package com.example.estiamapp.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.estiamapp.data.model.CategoryDto
import com.example.estiamapp.data.model.ProductDto
import com.example.estiamapp.ui.theme.EstiamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: ProductDto,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image
            AsyncImage(
                model = product.images.firstOrNull() ?: "",
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Titre
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Catégorie
                Text(
                    text = product.category.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Prix
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductCardPreview() {
    EstiamAppTheme {
        ProductCard(
            product = ProductDto(
                id = 1,
                title = "Amazing Product",
                slug = "amazing-product",
                price = 99.99,
                description = "This is an amazing product with lots of features that you will love.",
                category = CategoryDto(
                    id = 1,
                    name = "Electronics",
                    slug = "electronics",
                    image = "",
                    creationAt = "",
                    updatedAt = ""
                ),
                images = listOf(),
                creationAt = "",
                updatedAt = ""
            )
        )
    }
}