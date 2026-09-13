package com.example.ui.components

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VoiceConversationMessage
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceHighlight

@Composable
fun VoiceAssistantView(
  messages: List<VoiceConversationMessage>,
  isLoading: Boolean,
  isSpeaking: Boolean,
  onSendPrompt: (String) -> Unit,
  onSpeakText: (String) -> Unit,
  onStopSpeaking: () -> Unit,
  modifier: Modifier = Modifier
) {
  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  // Voice speech recognizer launcher
  val speechRecognizerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.resultCode == Activity.RESULT_OK && result.data != null) {
      val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
      if (!spoken.isNullOrBlank()) {
        onSendPrompt(spoken)
      }
    }
  }

  // Auto-scroll to latest message
  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  val quickPrompts = listOf(
    "What live news streams or breaking bulletins can I watch?",
    "What dark psychological thriller can I watch on Apple TV+ or Netflix?",
    "Top rural comedy on Prime Video or Sony LIV?",
    "Best sports highlights or cricket on JioHotstar?",
    "Fascinating science docuseries on YouTube?",
    "Compare RRR on ZEE5 vs Brahmāstra on Hotstar"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundDark)
  ) {
    // Model Banner & Live Wave Status Header
    Surface(
      color = SurfaceDark,
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, BorderSubtle)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AccentCyan)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Live Conversational API",
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Text(
            text = "Model: gemini-3.1-flash-live-preview",
            color = AccentCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }

        // Live visualizer bars / speaker status
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          if (isSpeaking) {
            IconButton(
              onClick = onStopSpeaking,
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color(0xFFE50914))
            ) {
              Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Stop voice",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }
          AnimatedWaveformIndicator(isActive = isLoading || isSpeaking)
        }
      }
    }

    // Messages Stream
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(messages, key = { it.id }) { msg ->
        VoiceMessageBubble(
          message = msg,
          onSpeak = { onSpeakText(msg.text) }
        )
      }

      if (isLoading) {
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(18.dp),
              color = AccentCyan,
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Gemini Live API is thinking...",
              color = Color(0xFFA0A7B8),
              fontSize = 12.sp
            )
          }
        }
      }
    }

    // Quick suggestion chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 12.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      quickPrompts.forEach { q ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceElevated)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .clickable { onSendPrompt(q) }
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(text = q, color = Color(0xFFC5CBD9), fontSize = 11.sp, maxLines = 1)
        }
      }
    }

    // Voice & Text Input Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(SurfaceDark)
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Big Mic Button for Speech-to-Text
      Surface(
        onClick = {
          val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Gemini Live Voice Assistant...")
          }
          try {
            speechRecognizerLauncher.launch(intent)
          } catch (e: Exception) {
            // Speech recognizer not available
          }
        },
        shape = CircleShape,
        color = AccentCyan,
        modifier = Modifier
          .size(46.dp)
          .testTag("voice_mic_trigger")
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Voice Input",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      OutlinedTextField(
        value = inputText,
        onValueChange = { inputText = it },
        placeholder = { Text("Ask voice assistant...", color = Color(0xFF6E7687), fontSize = 13.sp) },
        modifier = Modifier
          .weight(1f)
          .height(48.dp)
          .testTag("voice_text_input"),
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = SurfaceElevated,
          unfocusedContainerColor = SurfaceElevated,
          focusedBorderColor = AccentCyan,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        singleLine = true
      )

      Surface(
        onClick = {
          if (inputText.isNotBlank()) {
            val q = inputText
            inputText = ""
            onSendPrompt(q)
          }
        },
        shape = CircleShape,
        color = if (inputText.isNotBlank()) AccentGold else SurfaceElevated,
        modifier = Modifier
          .size(46.dp)
          .testTag("voice_send_button")
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = if (inputText.isNotBlank()) Color.Black else Color(0xFF6E7687),
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}

@Composable
fun VoiceMessageBubble(
  message: VoiceConversationMessage,
  onSpeak: () -> Unit
) {
  val isUser = message.role == "user"

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
  ) {
    Box(
      modifier = Modifier
        .clip(
          RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = if (isUser) 16.dp else 4.dp,
            bottomEnd = if (isUser) 4.dp else 16.dp
          )
        )
        .background(
          if (isUser) Brush.linearGradient(listOf(Color(0xFF0C5ADB), Color(0xFF00A8E1)))
          else Brush.linearGradient(listOf(SurfaceElevated, SurfaceHighlight))
        )
        .border(
          width = 1.dp,
          color = if (isUser) Color.Transparent else BorderSubtle,
          shape = RoundedCornerShape(16.dp)
        )
        .padding(12.dp)
    ) {
      Column {
        if (!isUser) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Gemini Live Assistant",
              color = AccentCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            IconButton(
              onClick = onSpeak,
              modifier = Modifier.size(22.dp)
            ) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Speak answer",
                tint = Color(0xFFA0A7B8),
                modifier = Modifier.size(16.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
          text = message.text,
          color = Color.White,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
      }
    }
  }
}

@Composable
fun AnimatedWaveformIndicator(isActive: Boolean) {
  val infiniteTransition = rememberInfiniteTransition(label = "wave")
  val height1 by infiniteTransition.animateFloat(
    initialValue = 6f,
    targetValue = if (isActive) 22f else 6f,
    animationSpec = infiniteRepeatable(
      animation = tween(400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "h1"
  )
  val height2 by infiniteTransition.animateFloat(
    initialValue = 12f,
    targetValue = if (isActive) 26f else 10f,
    animationSpec = infiniteRepeatable(
      animation = tween(320, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "h2"
  )
  val height3 by infiniteTransition.animateFloat(
    initialValue = 8f,
    targetValue = if (isActive) 20f else 8f,
    animationSpec = infiniteRepeatable(
      animation = tween(480, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "h3"
  )

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(3.dp),
    modifier = Modifier.height(26.dp)
  ) {
    Box(
      modifier = Modifier
        .width(3.dp)
        .height(height1.dp)
        .clip(RoundedCornerShape(2.dp))
        .background(AccentCyan)
    )
    Box(
      modifier = Modifier
        .width(3.dp)
        .height(height2.dp)
        .clip(RoundedCornerShape(2.dp))
        .background(AccentGold)
    )
    Box(
      modifier = Modifier
        .width(3.dp)
        .height(height3.dp)
        .clip(RoundedCornerShape(2.dp))
        .background(AccentCyan)
    )
  }
}
