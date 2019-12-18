package com.app.nasa.unofficial.ui.fragments

import com.app.nasa.unofficial.databinding.FragmentMarsBinding
import dagger.android.support.DaggerFragment

class MarsFragment : DaggerFragment() {

    private lateinit var binding: FragmentMarsBinding

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentMarsBinding.inflate(
//            inflater,
//            container,
//            false
//        )
//        showLog("Fragment Called")
//
//        return binding.root
//    }
}
