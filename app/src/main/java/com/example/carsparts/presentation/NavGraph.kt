package com.example.carsparts.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carsparts.presentation.addpart.CreateOrEditPartScreen
import com.example.carsparts.presentation.cars.CarsListScreen
import com.example.carsparts.presentation.parts.PartsListScreen
import com.example.carsparts.presentation.partsInfo.PartsInfoScreen
import com.example.carsparts.presentation.partsInfo.PartsInfoViewModel
import com.example.carsparts.viewmodels.CarsViewModel
import com.example.carsparts.viewmodels.PartsViewModel

@Composable
fun CarsPartsApp() {
    val navController = rememberNavController()
    CarsPartsNavHost(navController = navController)
}

@Composable
fun CarsPartsNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "carsList") {
        composable("carsList") {
            val carsViewModel: CarsViewModel = hiltViewModel()
            CarsListScreen(
                onCarSelected = { car ->
                    navController.navigate("partsList/${car.id}/${car.brand}/${car.model}")
                },
                viewModel = carsViewModel
            )
        }

        composable("partsList/{carId}/{brand}/{model}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull() ?: 0
            val brand = backStackEntry.arguments?.getString("brand") ?: ""
            val model = backStackEntry.arguments?.getString("model") ?: ""

            val partsViewModel: PartsViewModel = hiltViewModel()
            PartsListScreen(
                carId = carId,
                brand = brand,
                model = model,
                onPartSelected = { part ->
                    navController.navigate("partInfo/${part.id}")
                },
                onAddPart = {
                    navController.navigate("createOrEditPart/$carId")
                },
                onEditPart = { part ->
                    navController.navigate("createOrEditPart/$carId/${part.id}")
                },
                viewModel = partsViewModel
            )
        }

        composable("createOrEditPart/{carId}") { backStackEntry ->
            Log.d("CarsPartsNavHost","NewPart")
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull() ?: 0

            CreateOrEditPartScreen(
                carId = carId,
                partToEdit = null, // Это создание новой запчасти
                onDismiss = {
                    navController.popBackStack() // Закрываем экран
                }
            )
        }

        composable("createOrEditPart/{carId}/{partId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull() ?: 0
            val partId = backStackEntry.arguments?.getString("partId")?.toIntOrNull() ?: 0

            val partsViewModel: PartsViewModel = hiltViewModel()
            val part by partsViewModel.getPartById(partId).collectAsState(initial = null)

            CreateOrEditPartScreen(
                carId = carId,
                partToEdit = part, // Это редактирование существующей запчасти
                onDismiss = {
                    navController.popBackStack() // Закрываем экран
                }
            )
        }

//        composable("partInfo/{partId}") { backStackEntry ->
//
//            val partId = backStackEntry.arguments?.getString("partId")?.toIntOrNull() ?: 0
//            val partsInfoViewModel: PartsInfoViewModel = hiltViewModel()
//            partsInfoViewModel.loadPartInfo(partId)
//            val part by partsInfoViewModel.part.collectAsState(initial = null)
//            part?.let {
//                PartsInfoScreen(
//                    partId = partId
//                )
//            }
//        }

        composable("partInfo/{partId}") { backStackEntry ->
            val partId = backStackEntry.arguments?.getString("partId")?.toIntOrNull() ?: 0
            val partsInfoViewModel: PartsInfoViewModel = hiltViewModel()
            partsInfoViewModel.loadPartInfo(partId)
            val part by partsInfoViewModel.part.collectAsState(initial = null)

            part?.let {
                PartsInfoScreen(
                    partId = partId,
                    onEdit = { partToEdit ->
                        navController.navigate("createOrEditPart/${partToEdit.carId}/${partToEdit.id}")
                    }
                )
            }
        }

    }
}

