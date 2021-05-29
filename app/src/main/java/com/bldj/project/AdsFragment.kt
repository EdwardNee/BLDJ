package com.bldj.project

import android.R
import android.R.attr.data
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bldj.project.adapters.AdAdapter
import com.bldj.project.databinding.FragmentAdsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import data.Advert
import java.util.*
import kotlin.collections.ArrayList


/**
 * Фрагмент для окна объявлений.
 */
class AdsFragment : Fragment() {

    private var auth: FirebaseAuth? = null
    private var database: FirebaseDatabase? = null
    private var usersDbRef: DatabaseReference? = null
    private lateinit var listAds: ArrayList<Advert>
    private var usersChildEventListener: ChildEventListener? = null
    private lateinit var adAdapter: AdAdapter
    private lateinit var adsFragmentBinding: FragmentAdsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAds = ArrayList()

        //var listView: RecyclerView? = view?.findViewById(R.id.)
        usersDbRef =
            FirebaseDatabase.getInstance().reference.child(
                "adverts"
            )
        var user: FirebaseUser? = auth?.currentUser
        adAdapter = AdAdapter(listAds)

        updateAds()
//        adsFragmentBinding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (!adsFragmentBinding.rvMovies.canScrollVertically(1)) {
//                    updateAds()
//                }
//            }
//        })

        //here data must be an instance of the class MarsDataProvider
        //here data must be an instance of the class MarsDataProvider
        //adsFragmentBinding.setMarsdata(data)

        if (listAds.isEmpty()) {
            updateAds()
        }
        listAds.add(
            Advert(
                "ads", "sdad", 3, 3, "sdas"
            )
        )
//        Log.i("Eduard", listAds.size.toString())
        //var listView: RecyclerView? = view?.findViewById(R.id.)
//        usersDbRef = FirebaseDatabase.getInstance().reference.child("USER")
//        var user: FirebaseUser? = auth?.currentUser
//        adAdapter = AdAdapter(listAds)
//        adsFragmentBinding.apply {
//            adsFragmentBinding.rvMovies.adapter = adAdapter
//            invalidateAll()
//        }


    }

    private fun updateAds() {
        usersChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val ad: Advert = snapshot.getValue(Advert::class.java)!!
                if (!listAds.contains(ad)) {
                    listAds.add(ad)
                    Log.i("ads","dobavil")
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }
        usersDbRef?.addChildEventListener(usersChildEventListener!!)
        adAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        adAdapter.notifyDataSetChanged()
        Log.i("Eduard", listAds.size.toString())
        updateAds()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adsFragmentBinding = FragmentAdsBinding.inflate(
            inflater, container, false
        )
        val view: View = adsFragmentBinding.root
        adsFragmentBinding.apply {
            adsFragmentBinding.rvMovies.adapter = adAdapter
            invalidateAll()
        }
        adsFragmentBinding.rvMovies.setHasFixedSize(true)
        adsFragmentBinding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!adsFragmentBinding.rvMovies.canScrollVertically(1)) {
                    updateAds()
                }
            }
        })

        return view
        //return inflater.inflate(R.layout.fragment_ads, container, false)
    }
}