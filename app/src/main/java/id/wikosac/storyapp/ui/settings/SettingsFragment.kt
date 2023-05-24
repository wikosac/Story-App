package id.wikosac.storyapp.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.wikosac.storyapp.MainActivity
import id.wikosac.storyapp.databinding.FragmentSettingsBinding
import id.wikosac.storyapp.ui.auth.LoginActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = requireActivity().getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("NAME", "").toString()

        val textView: TextView = binding.textView
        textView.text = name

        binding.logout.setOnClickListener {
            showLogoutConfirmationDialog()
            clearLoginSession()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearLoginSession() {
        val sharedPreferences = requireContext().getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("TOKEN")
        editor.apply()
    }

    fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Apakah Anda yakin ingin logout?")
        builder.setPositiveButton("Ya") { _, _ ->
            performLogout()
        }
        builder.setNegativeButton("Tidak", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun performLogout() {
        Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

}