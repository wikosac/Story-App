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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: TextView = binding.textView
        textView.text = LoginActivity.NAME_PREF
        binding.logout.setOnClickListener { confirmLogout() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun confirmLogout() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ -> logout() }
        builder.setNegativeButton("No", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun logout() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
        deleteLoginSession()
        Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show()
    }

    private fun deleteLoginSession() {
        val sharedPreferences = requireContext().getSharedPreferences(LoginActivity.SESSION, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(LoginActivity.TOKEN)
        editor.remove(LoginActivity.NAME)
        editor.apply()
    }
}