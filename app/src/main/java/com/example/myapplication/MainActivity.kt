package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterForm()
        }
    }
}

data class UserData(
    var name: String = "",
    var gender: String = "",
    var phone: String = "",
    var address: String = ""
)

class RegisterVm : ViewModel() {
    var userData = mutableStateOf(UserData())

    fun updateUserName(input: String) {
        userData.value = userData.value.copy(name = input)
    }

    fun updateUserGender(input: String) {
        userData.value = userData.value.copy(gender = input)
    }

    fun updateUserPhone(input: String) {
        userData.value = userData.value.copy(phone = input)
    }

    fun updateUserAddress(input: String) {
        userData.value = userData.value.copy(address = input)
    }
}

@Composable
fun RegisterForm() {
    val viewModel: RegisterVm = viewModel()
    val allFieldsFilled = rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier
            .padding(10.dp)
            .background(colorResource(id = R.color.white))
            .fillMaxWidth()
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .shadow(1.dp, shape = RectangleShape)
    ) {
        Row {
            Text(
                text = "Register Form",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
        }

        TextFieldComponent(
            placeholder = "Enter your name",
            label = "Full Name",
            onTextChange = { viewModel.updateUserName(it) },
            event = "username",
            vm = viewModel,
            modifier = Modifier.fillMaxWidth(),
            allFieldsFilled = allFieldsFilled
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            TextFieldComponent(
                placeholder = "Gender",
                label = "Gender",
                onTextChange = { viewModel.updateUserGender(it) },
                event = "gender",
                vm = viewModel,
                modifier = Modifier.weight(0.5f),
                allFieldsFilled = allFieldsFilled
            )

            Spacer(modifier = Modifier.padding(5.dp))

            TextFieldComponent(
                placeholder = "Phone",
                label = "Phone",
                onTextChange = { viewModel.updateUserPhone(it) },
                event = "phone",
                vm = viewModel,
                modifier = Modifier.weight(1f),
                allFieldsFilled = allFieldsFilled
            )
        }

        TextFieldComponent(
            placeholder = "Address",
            label = "Address",
            onTextChange = { viewModel.updateUserAddress(it) },
            event = "address",
            vm = viewModel,
            modifier = Modifier.fillMaxWidth(),
            allFieldsFilled = allFieldsFilled
        )

        Button(
            onClick = {   },
            enabled = allFieldsFilled.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "Register", fontSize = 16.sp)
        }
    }
}

@Composable
fun TextFieldComponent(
    placeholder: String,
    label: String,
    onTextChange: (text: String) -> Unit,
    event: String,
    vm: RegisterVm,
    modifier: Modifier,
    allFieldsFilled: MutableState<Boolean>
) {
    OutlinedTextField(
        value = when (event) {
            "username" -> vm.userData.value.name
            "gender" -> vm.userData.value.gender
            "phone" -> vm.userData.value.phone
            else -> vm.userData.value.address
        },
        onValueChange = {
            onTextChange(it)
            updateButtonState(vm, allFieldsFilled)
        },
        placeholder = {
            Text(text = placeholder, fontSize = 16.sp)
        },
        label = { Text(text = label) },
        maxLines = 4,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (event=="phone") KeyboardType.Phone else KeyboardType.Text
        ),
        modifier = modifier
    )
}

private fun updateButtonState(vm: RegisterVm, allFieldsFilled: MutableState<Boolean>) {
    allFieldsFilled.value = vm.userData.value.name.isNotBlank() &&
            vm.userData.value.gender.isNotBlank() &&
            vm.userData.value.phone.isNotBlank() &&
            vm.userData.value.address.isNotBlank()
}
