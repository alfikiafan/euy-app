package com.example.myapplication.halaman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DataDummy.DataFood
import com.example.myapplication.R
import com.example.myapplication.adapter.ItemFoodAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [Receipe.newInstance] factory method to
 * create an instance of this fragment.
 */
class Receipe : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var  adapter:ItemFoodAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var newfoodArrayList: ArrayList<DataFood>


    lateinit var food: Array<String>
    lateinit var imageTittle: Array<Int>
    lateinit var nameTittle: Array<String>
    lateinit var desc:Array<String>
    lateinit var imageClock:Array<Int>

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
        return inflater.inflate(R.layout.fragment_receipe, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Receipe.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Receipe().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.rvMakanan)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = ItemFoodAdapter(newfoodArrayList)
        recyclerView.adapter = adapter
    }
    private fun dataInitialize(){
        newfoodArrayList = arrayListOf<DataFood>()

        imageTittle = arrayOf(
            R.drawable.rice,
                    R.drawable.rice,
                    R.drawable.rice,
                    R.drawable.rice,
                    R.drawable.rice,
                    R.drawable.rice,
        )
        imageClock = arrayOf(
            R.drawable.clock,
            R.drawable.clock,
            R.drawable.clock,
            R.drawable.clock,
            R.drawable.clock,
            R.drawable.clock,
        )
        nameTittle = arrayOf(
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
            getString(R.string.nama_makanan),
        )
        desc = arrayOf(
            getString(R.string.req_desc),
            getString(R.string.req_desc),
            getString(R.string.req_desc),
            getString(R.string.req_desc),
            getString(R.string.req_desc),
            getString(R.string.req_desc),
            )
        for (i in imageTittle.indices){

            val food = DataFood(nameTittle[i],imageTittle[i],imageClock[i],desc[i])
            newfoodArrayList.add(food)
        }
    }

}