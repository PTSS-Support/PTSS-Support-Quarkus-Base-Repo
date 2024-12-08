package org.ptss.support.infrastructure.repositories

import com.azure.data.tables.TableClient
import com.azure.data.tables.TableServiceClientBuilder
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.models.Product
import org.ptss.support.domain.interfaces.repositories.IProductRepository
import org.ptss.support.infrastructure.config.AzureStorageConfig
import org.ptss.support.infrastructure.persistence.entities.ProductEntity
import org.slf4j.LoggerFactory

@ApplicationScoped
class ProductRepository(
    private val azureConfig: AzureStorageConfig
) : IProductRepository {
    private val logger = LoggerFactory.getLogger(ProductRepository::class.java)
    private lateinit var tableClient: TableClient

    @PostConstruct
    fun initialize() {
        val tableServiceClient = TableServiceClientBuilder()
            .connectionString(azureConfig.connectionString())
            .buildClient()

        // Create table if it doesn't exist
        try {
            tableServiceClient.createTableIfNotExists(azureConfig.tableName())
        } catch (e: Exception) {
            logger.error("Error creating table: ${e.message}")
            throw e
        }

        tableClient = tableServiceClient.getTableClient(azureConfig.tableName())
    }

    override fun create(product: Product): String {
        val productEntity = ProductEntity(
            name = product.name,
            description = product.description,
            media = product.media.joinToString(",")
        )
        val tableEntity = productEntity.toTableEntity(product)
        tableClient.createEntity(tableEntity)
        return product.id
    }

    override fun getById(id: String): Product? {
        return try {
            val entity = tableClient.getEntity("PRODUCT", id)
            val productEntity = ProductEntity.fromTableEntity(entity)
            productEntity.toDomain().copy(id = id) // Set the ID from the table entity
        } catch (e: Exception) {
            logger.error("Error retrieving product $id: ${e.message}")
            null
        }
    }

    override fun getAll(): List<Product> {
        return tableClient.listEntities()
            .map { entity ->
                val productEntity = ProductEntity.fromTableEntity(entity)
                productEntity.toDomain().copy(id = entity.rowKey)
            }
            .toList()
    }
}