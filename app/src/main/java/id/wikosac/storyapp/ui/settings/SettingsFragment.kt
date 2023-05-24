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

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = requireActivity().getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("NAME", "").toString()

        val textView: TextView = binding.textView
        textView.text = name

        binding.logout.setOnClickListener { showLogoutConfirmationDialog() }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ -> performLogout() }
        builder.setNegativeButton("No", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun performLogout() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
        clearLoginSession()
        Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show()
    }

    private fun clearLoginSession() {
        val sharedPreferences = requireContext().getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("TOKEN")
        editor.remove("NAME")
        editor.apply()
    }
}