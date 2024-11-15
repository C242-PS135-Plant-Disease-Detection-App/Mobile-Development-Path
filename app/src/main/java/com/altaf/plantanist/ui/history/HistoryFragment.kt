package com.altaf.plantanist.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.altaf.plantanist.databinding.FragmentHistoryBinding
import com.altaf.plantanist.data.HistoryEntity

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        // Setup RecyclerView
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(context)
        historyAdapter = HistoryAdapter()
        binding.recyclerViewHistory.adapter = historyAdapter

        // Observe history data
        historyViewModel.allHistory.observe(viewLifecycleOwner) { historyList ->
            historyAdapter.submitList(historyList)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
