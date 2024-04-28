package com.devdreamerx.mind_sync.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.devdreamerx.mind_sync.databinding.FragmentDashboardBinding
import com.devdreamerx.mind_sync.util.PredictionResponseEvent
import com.devdreamerx.mind_sync.viewModel.DataViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayInputStream
import java.io.InputStream


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val dataViewModel: DataViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EventBus.getDefault().register(this)

        dataViewModel.name.observe(viewLifecycleOwner) { name ->
            Log.d("DataViewModel", "Name from DataViewModel: $name")
            binding.nameResults.text = name
        }

    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onPredictionResponseEvent(event: PredictionResponseEvent){
        val prediction = event.prediction

        binding.predictionYN.text = prediction.prediction.toString()
        binding.typeText.text = prediction.adhdType.toString()
        val recommendations = when (prediction.adhdType) {
            "Typically Developing Children" -> {
                """
            1. Provide opportunities for unstructured play and exploration.
            Foster curiosity and creativity through toys, games, and outdoor activities.

            2. Emphasize the importance of balanced nutrition and regular physical activity.
            Establish consistent sleep routines to ensure adequate rest and rejuvenation.

            3. Encourage positive social interactions with peers and family members.
            Teach empathy, kindness, and effective communication skills.

            4. Offer age-appropriate responsibilities and encourage self-help skills.
            Provide opportunities for decision-making and problem-solving.

            5. Read together regularly and engage in conversations about books and stories.
            Provide educational toys, puzzles, and games to promote cognitive development.

            6. Acknowledge and celebrate your child's efforts and accomplishments.
            Encourage a growth mindset by praising persistence and resilience.
            """
            }
            "ADHD-Combined" -> {
                """
            1. Establish a consistent daily routine and stick to it. Use planners or digital calendars to organize tasks and deadlines.
            2. Divide larger tasks into smaller, manageable steps. Focus on completing one step at a time to avoid feeling overwhelmed.
            3. Designate a distraction-free workspace for work or study. Use noise-canceling headphones or background music to stay focused.
            4. Define specific, achievable goals for each day or week. Prioritize tasks based on importance and urgency to stay on track.
            5. Set timers or alarms to help manage time and stay on schedule. Utilize apps or tools designed for task management and organization.
            6. Reach out to friends, family, or support groups for encouragement and understanding. Consider therapy or coaching to learn coping strategies and develop effective skills.
            """
            }
            "ADHD-Hyperactive/Impulsive" -> {
                """
            1. Engage in regular physical activities or sports to release excess energy. Encourage participation in structured activities that require focus, such as martial arts or dance.
            2. Learn to recognize impulsive urges and pause before acting on them. Practice mindfulness techniques to increase self-awareness and impulse control.
            3. Utilize visual aids, timers, or alarms to help maintain focus and manage time effectively. Create visual schedules or checklists to guide through tasks and routines.
            4. Identify triggers that lead to impulsive behavior and develop coping mechanisms to manage them. Practice deep breathing or relaxation techniques to calm the mind during moments of impulsivity.
            5. Set clear rules and expectations for behavior at home, school, and in social settings. Consistently enforce consequences for impulsive actions while also providing positive reinforcement for self-control.
            6. Consult with a healthcare professional experienced in treating ADHD to explore medication options if necessary. Consider therapy or counseling to learn additional coping skills and strategies for managing impulsivity and hyperactivity.
            """
            }
            "ADHD-Inattentive" -> {
                """
            1. Establish a consistent daily routine with designated times for tasks and activities. Use visual schedules or planners to help stay organized and on track.
            2. Break down larger tasks into smaller, more manageable chunks. Set specific goals for each step and focus on completing one task at a time.
            3. Designate a quiet, clutter-free workspace for tasks requiring concentration. Use noise-canceling headphones or white noise machines to block out distractions.
            4. Set alarms, timers, or reminders on electronic devices to help maintain focus and remember important tasks. Use sticky notes or visual cues to prompt attention to specific tasks or deadlines.
            5. Practice mindfulness or meditation to improve attention and focus. Use techniques such as the Pomodoro Technique (work for a set time, then take a short break) to manage attention span.
            6. Communicate with teachers, employers, or colleagues about your ADHD and request accommodations if needed (e.g., extended time for tasks, preferential seating). Consider therapy or coaching to learn additional strategies for improving attention and organizational skills.
            """
            }
            else -> "No recommendations available"
        }

        binding.txtRecommendations.text = recommendations

    }

    private fun convertBase64ToBitmap(base64String: String): Bitmap? {
        val decodedBytes: ByteArray = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)

        val inputStream: InputStream = ByteArrayInputStream(decodedBytes)
        return BitmapFactory.decodeStream(inputStream)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        EventBus.getDefault().unregister(this)
    }
}
