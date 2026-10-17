package com.example.studentattendanceregister

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Student(
    val name: String,
    val present: Boolean? = null
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AttendanceScreen()
        }
    }
}

@Composable
fun AttendanceScreen() {
    val context = LocalContext.current

    var studentName by remember { mutableStateOf("") }
    val students = remember { mutableStateListOf<Student>() }

    val total = students.size
    val present = students.count { it.present == true }
    val absent = students.count { it.present == false }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Student Attendance Register",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF512DA8)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = studentName,
            onValueChange = { studentName = it },
            label = { Text("Enter student's name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (studentName.isBlank()) {
                    Toast.makeText(
                        context,
                        "Please enter a student's name",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    students.add(
                        Student(studentName.trim())
                    )
                    studentName = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Student")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Students",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (students.isEmpty()) {
            Text(
                text = "No students have been added yet."
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            itemsIndexed(students) { index, student ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "${index + 1}. ${student.name}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )

                        Text(
                            text = when (student.present) {
                                true -> "Present"
                                false -> "Absent"
                                null -> "Not marked"
                            },
                            color = when (student.present) {
                                true -> Color(0xFF2E7D32)
                                false -> Color(0xFFC62828)
                                null -> Color.Gray
                            }
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    students[index] =
                                        student.copy(present = true)
                                }
                            ) {
                                Text("Present")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    students[index] =
                                        student.copy(present = false)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFC62828)
                                )
                            ) {
                                Text("Absent")
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider()

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Attendance Summary",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        Text("Total Students: $total")
        Text("Present: $present", color = Color(0xFF2E7D32))
        Text("Absent: $absent", color = Color(0xFFC62828))

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                students.clear()
                studentName = ""
            }
        ) {
            Text("Reset Register")
        }
    }
}