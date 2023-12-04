package com.example.myapplication.halaman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DataDummy.DataFood
import com.example.myapplication.DataDummy.DataRecom
import com.example.myapplication.R
import com.example.myapplication.adapter.ItemFoodAdapter
import com.example.myapplication.adapter.ItemRecomAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Utama.newInstance] factory method to
 * create an instance of this fragment.
 */
class Utama : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var  adapter: ItemRecomAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var newrecomArrayList: ArrayList<DataRecom>

    lateinit var photoRecom: Array<Int>
    lateinit var nameRecom: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_utama, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Utama.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Utama().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        recyclerView = view.findViewById(R.id.rv_rekom)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        adapter = ItemRecomAdapter(newrecomArrayList)
        recyclerView.adapter = adapter
    }

    private fun dataInitialize(){
        newrecomArrayList = arrayListOf<DataRecom>()

        photoRecom = arrayOf(
            R.drawable.nasigoreng,
            R.drawable.nasigoreng,
            R.drawable.nasigoreng,
            R.drawable.nasigoreng,
            R.drawable.nasigoreng,
            R.drawable.nasigoreng,
        )
        nameRecom = arrayOf(
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
        )
        for (i in photoRecom.indices){
            val food = DataRecom(nameRecom[i],photoRecom[i])
            newrecomArrayList.add(food)
        }
    }
}
