package com.example.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceHighlight
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class LoginStep {
  NAME,
  PHONE,
  OTP,
  SUCCESS
}

@Composable
fun LoginScreen(
  onLoginSuccess: (name: String, phoneNumber: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var currentStep by remember { mutableStateOf(LoginStep.NAME) }
  var nameInput by remember { mutableStateOf("") }
  var phoneInput by remember { mutableStateOf("") }
  var otpInput by remember { mutableStateOf("") }
  var generatedOtp by remember { mutableStateOf("") }
  var isSendingOtp by remember { mutableStateOf(false) }
  var isVerifyingOtp by remember { mutableStateOf(false) }
  var otpError by remember { mutableStateOf<String?>(null) }
  var showSmsBanner by remember { mutableStateOf(false) }
  var resendTimer by remember { mutableIntStateOf(30) }

  val keyboardController = LocalSoftwareKeyboardController.current

  // Countdown timer for resend OTP
  LaunchedEffect(currentStep, resendTimer) {
    if (currentStep == LoginStep.OTP && resendTimer > 0) {
      delay(1000)
      resendTimer--
    }
  }

  fun generateAndSendOtp() {
    isSendingOtp = true
    otpError = null
    // Generate 6 digit OTP
    val code = String.format("%06d", Random.nextInt(100000, 999999))
    generatedOtp = code
    otpInput = ""
    resendTimer = 30

    // Simulate sending delay
    showSmsBanner = false
    isSendingOtp = false
    currentStep = LoginStep.OTP
    showSmsBanner = true
  }

  fun verifyOtp(code: String) {
    if (code.length != 6) {
      otpError = "Please enter the complete 6-digit OTP code."
      return
    }

    isVerifyingOtp = true
    if (code == generatedOtp) {
      otpError = null
      currentStep = LoginStep.SUCCESS
      keyboardController?.hide()
    } else {
      isVerifyingOtp = false
      otpError = "Invalid OTP code. Please check the code and try again."
    }
  }

  LaunchedEffect(currentStep) {
    if (currentStep == LoginStep.SUCCESS) {
      delay(1200)
      onLoginSuccess(nameInput.trim(), phoneInput.trim())
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundDark)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(24.dp))

      // Top App Branding
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(RoundedCornerShape(18.dp))
          .background(
            Brush.linearGradient(
              listOf(Color(0xFFE50914), Color(0xFF00A8E1), Color(0xFFFFB800))
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = "OTT Logo",
          tint = Color.White,
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "OTT Stream Hub",
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White,
        letterSpacing = 0.5.sp
      )

      Text(
        text = "YouTube • Hotstar • Apple TV+ • Netflix • ZEE5 • Sony LIV • Prime",
        fontSize = 11.sp,
        color = AccentGold,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
      )

      Spacer(modifier = Modifier.height(28.dp))

      // Multi-Step Progress Tracker
      StepProgressRow(currentStep = currentStep)

      Spacer(modifier = Modifier.height(28.dp))

      // Simulated Push Notification Banner for OTP
      AnimatedVisibility(
        visible = showSmsBanner && currentStep == LoginStep.OTP,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
      ) {
        Card(
          colors = CardDefaults.cardColors(containerColor = SurfaceHighlight),
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.6f)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .testTag("sms_notification_banner")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Sms,
                  contentDescription = "SMS",
                  tint = AccentCyan,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "SMS • OTT-VERIFY",
                  color = AccentCyan,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Text(
                text = "Just now",
                color = Color(0xFFA0A7B8),
                fontSize = 10.sp
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Your one-time passcode for OTT Stream Hub is $generatedOtp. Valid for 5 minutes. Do not share with anyone.",
              color = Color.White,
              fontSize = 12.sp,
              lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              TextButton(
                onClick = {
                  otpInput = generatedOtp
                  verifyOtp(generatedOtp)
                },
                modifier = Modifier.testTag("autofill_otp_button")
              ) {
                Icon(
                  imageVector = Icons.Default.MarkEmailRead,
                  contentDescription = null,
                  tint = AccentGold,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Auto-Fill & Verify ($generatedOtp)",
                  color = AccentGold,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
            }
          }
        }
      }

      // Main Step Card with Animated Transitions
      Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
              if (targetState.ordinal > initialState.ordinal) {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                  slideOutHorizontally { width -> -width } + fadeOut()
                )
              } else {
                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                  slideOutHorizontally { width -> width } + fadeOut()
                )
              }
            },
            label = "LoginStepTransition"
          ) { step ->
            when (step) {
              LoginStep.NAME -> {
                NameStepContent(
                  name = nameInput,
                  onNameChange = { nameInput = it },
                  onNext = {
                    if (nameInput.trim().length >= 2) {
                      currentStep = LoginStep.PHONE
                    }
                  }
                )
              }
              LoginStep.PHONE -> {
                PhoneStepContent(
                  name = nameInput.trim(),
                  phone = phoneInput,
                  onPhoneChange = { phoneInput = it.filter { char -> char.isDigit() }.take(10) },
                  onBack = { currentStep = LoginStep.NAME },
                  onSendOtp = { generateAndSendOtp() },
                  isLoading = isSendingOtp
                )
              }
              LoginStep.OTP -> {
                OtpStepContent(
                  phone = phoneInput,
                  otp = otpInput,
                  onOtpChange = {
                    val filtered = it.filter { char -> char.isDigit() }.take(6)
                    otpInput = filtered
                    otpError = null
                    if (filtered.length == 6) {
                      verifyOtp(filtered)
                    }
                  },
                  onBack = {
                    currentStep = LoginStep.PHONE
                    showSmsBanner = false
                  },
                  onVerify = { verifyOtp(otpInput) },
                  onResendOtp = { generateAndSendOtp() },
                  resendTimer = resendTimer,
                  isVerifying = isVerifyingOtp,
                  errorMessage = otpError
                )
              }
              LoginStep.SUCCESS -> {
                SuccessStepContent(name = nameInput.trim())
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      // Footer Privacy notice
      Text(
        text = "By signing in, you agree to access unified watchlists and streaming links. We keep your credentials secure.",
        fontSize = 11.sp,
        color = Color(0xFF6E7687),
        textAlign = TextAlign.Center,
        lineHeight = 15.sp,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
    }
  }
}

@Composable
fun StepProgressRow(currentStep: LoginStep) {
  val steps = listOf("Name", "Phone", "OTP")
  val activeIndex = when (currentStep) {
    LoginStep.NAME -> 0
    LoginStep.PHONE -> 1
    LoginStep.OTP, LoginStep.SUCCESS -> 2
  }

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    steps.forEachIndexed { index, label ->
      val isPast = index < activeIndex
      val isCurrent = index == activeIndex

      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(
              when {
                isPast -> AccentCyan
                isCurrent -> AccentGold
                else -> SurfaceElevated
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          if (isPast) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = BackgroundDark,
              modifier = Modifier.size(16.dp)
            )
          } else {
            Text(
              text = "${index + 1}",
              color = if (isCurrent) BackgroundDark else Color(0xFFA0A7B8),
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
          text = label,
          color = when {
            isPast -> AccentCyan
            isCurrent -> Color.White
            else -> Color(0xFFA0A7B8)
          },
          fontSize = 12.sp,
          fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
        )

        if (index < steps.size - 1) {
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .width(24.dp)
              .height(2.dp)
              .background(if (index < activeIndex) AccentCyan else BorderSubtle)
          )
          Spacer(modifier = Modifier.width(8.dp))
        }
      }
    }
  }
}

@Composable
fun NameStepContent(
  name: String,
  onNameChange: (String) -> Unit,
  onNext: () -> Unit
) {
  val isValid = name.trim().length >= 2

  Column(modifier = Modifier.fillMaxWidth()) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(SurfaceElevated),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = AccentGold,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "Step 1: Your Name",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "How should we greet you?",
          fontSize = 12.sp,
          color = Color(0xFFA0A7B8)
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    OutlinedTextField(
      value = name,
      onValueChange = onNameChange,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("input_user_name"),
      label = { Text("Full Name") },
      placeholder = { Text("e.g. Alex Sharma") },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = if (isValid) AccentGold else Color(0xFFA0A7B8)
        )
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
      ),
      keyboardActions = KeyboardActions(
        onNext = {
          if (isValid) onNext()
        }
      ),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = SurfaceElevated,
        unfocusedContainerColor = SurfaceElevated,
        focusedBorderColor = AccentGold,
        unfocusedBorderColor = BorderSubtle,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
      ),
      shape = RoundedCornerShape(14.dp)
    )

    Spacer(modifier = Modifier.height(22.dp))

    Button(
      onClick = onNext,
      enabled = isValid,
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("name_next_button"),
      colors = ButtonDefaults.buttonColors(
        containerColor = AccentGold,
        contentColor = Color.Black,
        disabledContainerColor = SurfaceElevated,
        disabledContentColor = Color(0xFF6E7687)
      ),
      shape = RoundedCornerShape(14.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = "Continue to Mobile Number",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
fun PhoneStepContent(
  name: String,
  phone: String,
  onPhoneChange: (String) -> Unit,
  onBack: () -> Unit,
  onSendOtp: () -> Unit,
  isLoading: Boolean
) {
  val isValidPhone = phone.length == 10

  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = Color.White
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "Step 2: Phone Number",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "Welcome, $name! Enter your mobile number",
          fontSize = 12.sp,
          color = Color(0xFFA0A7B8)
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    OutlinedTextField(
      value = phone,
      onValueChange = onPhoneChange,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("input_user_phone"),
      label = { Text("Mobile Number") },
      placeholder = { Text("9876543210") },
      leadingIcon = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(start = 12.dp, end = 6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = null,
            tint = if (isValidPhone) AccentCyan else Color(0xFFA0A7B8),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "+91",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.width(6.dp))
          Box(
            modifier = Modifier
              .width(1.dp)
              .height(18.dp)
              .background(BorderSubtle)
          )
        }
      },
      trailingIcon = {
        if (isValidPhone) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Valid",
            tint = AccentCyan
          )
        }
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(
        onDone = {
          if (isValidPhone && !isLoading) onSendOtp()
        }
      ),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = SurfaceElevated,
        unfocusedContainerColor = SurfaceElevated,
        focusedBorderColor = AccentCyan,
        unfocusedBorderColor = BorderSubtle,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
      ),
      shape = RoundedCornerShape(14.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "We will send a 6-digit one-time password (OTP) via SMS to verify this number.",
      fontSize = 11.sp,
      color = Color(0xFF8E95A5),
      lineHeight = 15.sp
    )

    Spacer(modifier = Modifier.height(22.dp))

    Button(
      onClick = onSendOtp,
      enabled = isValidPhone && !isLoading,
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("send_otp_button"),
      colors = ButtonDefaults.buttonColors(
        containerColor = AccentCyan,
        contentColor = Color.Black,
        disabledContainerColor = SurfaceElevated,
        disabledContentColor = Color(0xFF6E7687)
      ),
      shape = RoundedCornerShape(14.dp)
    ) {
      if (isLoading) {
        CircularProgressIndicator(
          color = Color.Black,
          modifier = Modifier.size(20.dp),
          strokeWidth = 2.dp
        )
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Sms,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Send Verification OTP",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
fun OtpStepContent(
  phone: String,
  otp: String,
  onOtpChange: (String) -> Unit,
  onBack: () -> Unit,
  onVerify: () -> Unit,
  onResendOtp: () -> Unit,
  resendTimer: Int,
  isVerifying: Boolean,
  errorMessage: String?
) {
  val focusRequester = remember { FocusRequester() }

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = Color.White
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "Step 3: Enter OTP",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = "Sent to +91 $phone",
          fontSize = 12.sp,
          color = AccentGold
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // 6-digit visual PIN boxes
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { focusRequester.requestFocus() },
      horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
      for (i in 0 until 6) {
        val digit = if (i < otp.length) otp[i].toString() else ""
        val isCurrentFocus = i == otp.length

        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceElevated)
            .border(
              width = if (isCurrentFocus) 2.dp else 1.dp,
              color = when {
                errorMessage != null -> Color(0xFFFF5252)
                isCurrentFocus -> AccentGold
                digit.isNotEmpty() -> AccentCyan
                else -> BorderSubtle
              },
              shape = RoundedCornerShape(10.dp)
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = digit,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    // Hidden input to capture numeric keyboard
    OutlinedTextField(
      value = otp,
      onValueChange = onOtpChange,
      modifier = Modifier
        .size(1.dp)
        .focusRequester(focusRequester)
        .testTag("input_otp_code"),
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.NumberPassword,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(
        onDone = {
          if (otp.length == 6) onVerify()
        }
      )
    )

    if (errorMessage != null) {
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = errorMessage,
        color = Color(0xFFFF5252),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Verify button
    Button(
      onClick = onVerify,
      enabled = otp.length == 6 && !isVerifying,
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("verify_otp_button"),
      colors = ButtonDefaults.buttonColors(
        containerColor = AccentGold,
        contentColor = Color.Black,
        disabledContainerColor = SurfaceElevated,
        disabledContentColor = Color(0xFF6E7687)
      ),
      shape = RoundedCornerShape(14.dp)
    ) {
      if (isVerifying) {
        CircularProgressIndicator(
          color = Color.Black,
          modifier = Modifier.size(20.dp),
          strokeWidth = 2.dp
        )
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Verify & Complete Login",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Resend OTP Action
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (resendTimer > 0) {
        Text(
          text = "Resend OTP in ${resendTimer}s",
          color = Color(0xFFA0A7B8),
          fontSize = 12.sp
        )
      } else {
        TextButton(
          onClick = onResendOtp,
          modifier = Modifier.testTag("resend_otp_button")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            tint = AccentCyan,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Resend OTP Code",
            color = AccentCyan,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}

@Composable
fun SuccessStepContent(name: String) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .background(Color(0xFF10B981).copy(alpha = 0.2f))
        .border(2.dp, Color(0xFF10B981), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Success",
        tint = Color(0xFF10B981),
        modifier = Modifier.size(36.dp)
      )
    }

    Spacer(modifier = Modifier.height(18.dp))

    Text(
      text = "Verification Successful!",
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "Welcome aboard, $name! Tuning into 7 OTT platforms...",
      fontSize = 13.sp,
      color = Color(0xFFA0A7B8),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(20.dp))

    CircularProgressIndicator(
      color = AccentGold,
      modifier = Modifier.size(24.dp),
      strokeWidth = 2.5.dp
    )
  }
}
