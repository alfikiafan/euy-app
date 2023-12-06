package com.android.euy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.euy.R
import com.android.euy.databinding.FragmentPantriBinding
import com.android.euy.databinding.FragmentProfilBinding
import com.android.euy.ui.viewmodels.AuthViewModel


class ProfilFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfilBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = viewModel.getCurrentUser()

        user?.let {
            binding.txtEmail.text = it.email
            if (it.displayName.isNullOrEmpty()){
                viewModel.getUserFromDB(it.email!!).addOnSuccessListener {result->
                    for (document in result) {
                        val name = document.getString("name")
                        binding.txtNama.text= name
                        binding.txtToolbarNama.text= name
                        break
                    }
                }
            }else{
                binding.txtNama.text= it.displayName
                binding.txtToolbarNama.text= it.displayName
            }
        }

        binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
            this@ProfilFragment.activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}