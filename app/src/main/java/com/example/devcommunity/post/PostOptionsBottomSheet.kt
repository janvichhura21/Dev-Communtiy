package com.example.devcommunity.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.devcommunity.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PostOptionsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var linkEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_options_bottom_sheet, container, false)
        linkEditText = view.findViewById(R.id.linkEditText)

        val attachLinkButton: Button = view.findViewById(R.id.attachLinkButton)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)

        attachLinkButton.setOnClickListener {
            val link = linkEditText.text.toString()
            val intent =Intent(requireContext(),ScannerActivity::class.java)
            intent.putExtra("link",link)
            startActivity(intent)
            // Attach the link to the post or perform any desired action
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss() // Close the bottom sheet
        }

        return view
    }
}
