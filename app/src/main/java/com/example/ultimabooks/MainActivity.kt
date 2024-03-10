package com.example.ultimabooks


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.booksfinal20.data.BookItem
import com.example.booksfinal20.data.BookRepository
import com.example.booksfinal20.data.BookViewModel
import com.example.booksfinal20.data.BookViewModelFactory
import com.example.booksfinal20.data.RetrofitInstance.bookApi
import com.example.ultimabooks.ui.theme.UltimaBooksTheme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UltimaBooksTheme {

                val repository = BookRepository(bookApi)
                val factory = BookViewModelFactory(repository)
                val viewModel: BookViewModel by viewModels { factory }

                AppNavigator(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigator(viewModel: BookViewModel) {
    val navController = rememberNavController()


    val booksState = viewModel.books.observeAsState(initial = emptyList())

    NavHost(navController, startDestination = "search") {
        composable("search") { SearchScreen(navController, viewModel) }
        composable("results") { ResultsScreen(navController, booksState) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: BookViewModel) {
    var searchTerm by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = searchTerm,
            onValueChange = { searchTerm = it },
            label = { Text("Search term") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.searchBooks(searchTerm)
                navController.navigate("results")
            }
        ) {
            Text("Search")
        }
    }
}

@Composable
fun ResultsScreen(navController: NavController, booksState: State<List<BookItem>>) {
    val booksList by booksState

    ResultsTopAppBar(navController)
    LazyColumn {
        items(booksList) { book ->
            BookListItem(book = book)
        }
    }
}

@Composable
fun BookListItem(book: BookItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = book.volumeInfo.imageLinks?.thumbnail),
            contentDescription = "Image",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = book.volumeInfo.title, style = MaterialTheme.typography.bodyMedium)
            book.volumeInfo.authors?.let { authors ->
                Text(text = authors.joinToString(", "), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ResultsTopAppBar(navController: NavController) {
    TopAppBar(
        title = { Text("Results") },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Filled.Home, contentDescription = "Back")
            }
        }
    )
}
