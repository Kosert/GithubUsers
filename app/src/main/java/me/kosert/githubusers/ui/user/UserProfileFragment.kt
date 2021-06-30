package me.kosert.githubusers.ui.user

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import me.kosert.githubusers.R
import me.kosert.githubusers.databinding.FragmentUserBinding
import me.kosert.githubusers.util.getRandomColor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserProfileFragment : Fragment(R.layout.fragment_user) {

    private val args by navArgs<UserProfileFragmentArgs>()
    private val binding by viewBinding(FragmentUserBinding::bind)

    private val viewModel: UserProfileViewModel by viewModel { parametersOf(args.userName) }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isProgress.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, R.string.network_error, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.userData.observe(viewLifecycleOwner) { user ->
            user ?: return@observe

            binding.apply {
                Glide.with(this@UserProfileFragment)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(avatarImageView)
                avatarImageView.backgroundTintList = ColorStateList.valueOf(getRandomColor())

                blogText.text = user.blog
                blogText.paintFlags = blogText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                blogText.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(user.blog)))
                }

                nameText.text = user.name
                nickText.text = "@${user.login}"
                locationText.text = getString(R.string.location_at, user.location)
                followingText.text = getString(R.string.following_count, user.following)
                followersText.text = getString(R.string.followers_count, user.followers)
                registerTimeText.text = getString(R.string.registered_at, user.createdAt)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

}