// HistoryFragment.kt
package com.altaf.plantanist.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.altaf.plantanist.api.ApiConfig
import com.altaf.plantanist.api.Prediction
import com.altaf.plantanist.data.AuthenticationDatabase
import com.altaf.plantanist.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchHistory()
    }

    private fun fetchHistory() {
        lifecycleScope.launch {
            val token = AuthenticationDatabase.getDatabase(requireContext()).tokenDao().getToken()?.token ?: return@launch
            val response = ApiConfig.getApiService().getHistory(token)
            if (response.isSuccessful) {
                val predictions = response.body()?.predictions ?: emptyList()
                setupRecyclerView(predictions)
            } else {
                Toast.makeText(requireContext(), "Error fetching history: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(predictions: List<Prediction>) {
        val adapter = HistoryAdapter(predictions)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}