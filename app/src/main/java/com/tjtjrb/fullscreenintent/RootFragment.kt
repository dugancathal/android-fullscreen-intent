package com.tjtjrb.fullscreenintent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_root.*

class RootFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_root, container, false)
    }

    override fun onResume() {
        super.onResume()
        hello_world_txt.setOnClickListener {
            ContextCompat.startForegroundService(
                requireContext(),
                Intent(requireContext(), FullScreenForegroundService::class.java)
            )
        }
    }
}
