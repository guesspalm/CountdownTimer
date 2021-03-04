/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

@Stable
val selectMinute: Int = 1

@Stable
val selectSecond: Int = 2

// Start building your app here!
@ExperimentalFoundationApi
@Composable
fun MyApp() {
    val selected = remember { mutableStateOf(selectSecond) }
    val minute = remember { mutableStateOf(0) }
    val second = remember { mutableStateOf(0) }
    val isCountingDown = remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colors.background) {
        if (isCountingDown.value) {
            val totalSecond = minute.value * 60 + second.value

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Box(
                    modifier = Modifier
                        .aspectRatio(1.0f)
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {

                    val bgColor = MaterialTheme.colors.onSurface
                    Canvas(
                        Modifier
                            .aspectRatio(1.0f)
                    ) {
                        drawCircle(
                            bgColor,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Butt)
                        )
                    }

                    val animationState: AnimationState<Float, AnimationVector1D> = remember {
                        AnimationState(0f)
                    }

                    LaunchedEffect(0) {
                        animationState.animateTo(
                            1f,
                            tween(totalSecond * 1000, easing = LinearEasing),
                            // If the previous animation was interrupted (i.e. not finished), make it sequential.
//                    !animationState.isFinished
                        )
                    }

                    CircularProgressIndicator(
                        progress = animationState.value,
                        modifier = Modifier
                            .aspectRatio(1.0f),
                        strokeWidth = 8.dp
                    )
                    val remain = totalSecond * (1 - animationState.value)
                    val remainInt = ceil(remain.toDouble()).toInt()
                    val s: Int = remainInt % 60
                    val m: Int = remainInt / 60

                    Text(
                        text = String.format("%02d:%02d", m, s),
                        style = TextStyle(
                            color = MaterialTheme.colors.onSurface,
                            fontWeight = FontWeight.Medium,
                            fontSize = 30.sp
                        ),
                    )
                }

                Button(onClick = { isCountingDown.value = false }) {
                    Image(
                        painter = painterResource(android.R.drawable.ic_menu_delete),
                        contentDescription = "Finish",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = BiasAlignment.Vertical(-0.3f)
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%02d", minute.value),
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    selected.value = selectMinute
                                },
                            style = TextStyle(
                                color = if (selected.value == selectMinute) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Medium,
                                fontSize = 50.sp
                            )
                        )
                        Text(
                            text = "Minute",
                            style = TextStyle(
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Text(
                        text = ":",
                        style = TextStyle(color = MaterialTheme.colors.onSurface, fontSize = 20.sp)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%02d", second.value),
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    selected.value = selectSecond
                                },
                            style = TextStyle(
                                color = if (selected.value == selectSecond) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.Medium,
                                fontSize = 50.sp
                            )
                        )
                        Text(
                            text = "Second",
                            style = TextStyle(
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
                NumberKeyBoard(
                    onClick = { number ->
                        if (number == -1) {
                            // DELETE
                            when (selected.value) {
                                selectMinute -> numberDelete(minute)
                                selectSecond -> numberDelete(second)
                            }
                        } else {
                            when (selected.value) {
                                selectMinute -> numberInput(current = minute, number = number)
                                selectSecond -> numberInput(current = second, number = number)
                            }
                        }
                    }
                )
                Button(
                    onClick = {
                        if (minute.value > 0 || second.value > 0) isCountingDown.value = true
                    }
                ) {
                    Image(
                        painter = painterResource(android.R.drawable.ic_media_play),
                        contentDescription = "start",
                    )
                }
            }
        }
    }
}

fun numberDelete(current: MutableState<Int>) {
    current.value = current.value / 10
}

fun numberInput(current: MutableState<Int>, number: Int) {
    val target = current.value * 10 + number
    if (target >= 100) {
        return
    }
    current.value = target
}

@ExperimentalFoundationApi
@Composable
// / onclick -1 is delete
fun NumberKeyBoard(onClick: (Int) -> Unit) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3), contentPadding = PaddingValues(20.dp),
        content = {
            items(12) { index ->
                if (index <= 8) {
                    NumberKeyBoardButton(index + 1, onClick = onClick)
                } else {
                    if (index == 10) {
                        NumberKeyBoardButton(0, onClick = onClick)
                    } else if (index == 11) {
                        // delete
                        Image(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { onClick(-1) },
                            painter = painterResource(android.R.drawable.ic_input_delete),
                            contentDescription = "delete",
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun NumberKeyBoardButton(num: Int, onClick: (Int) -> Unit) {
    Text(
        text = "$num",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onClick(num)
            },
        style = TextStyle(
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            lineHeight = 50.sp,
        )
    )
}

@ExperimentalFoundationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@ExperimentalFoundationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
