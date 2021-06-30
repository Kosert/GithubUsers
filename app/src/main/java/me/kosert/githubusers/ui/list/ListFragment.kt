package me.kosert.githubusers.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.kosert.githubusers.R
import me.kosert.githubusers.databinding.FragmentListBinding
import me.kosert.githubusers.util.Log
import me.kosert.githubusers.util.withFooter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ListFragment : Fragment(R.layout.fragment_list) {

    private val binding by viewBinding(FragmentListBinding::bind)

    private val viewModel by sharedViewModel<ListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usersAdapter = UserListAdapter(onUserClicked = { onUserClicked(it) })
        val loadStateAdapter = UserLoadStateAdapter(onClick = { usersAdapter.retry() })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usersAdapter.withFooter(loadStateAdapter)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingFlow.collectLatest { pagingData ->
                usersAdapter.submitData(pagingData)
            }
        }
    }

    private fun onUserClicked(userName: String) {
        val action = ListFragmentDirections.goToUserFragment(userName)
        findNavController().navigate(action)
    }
}