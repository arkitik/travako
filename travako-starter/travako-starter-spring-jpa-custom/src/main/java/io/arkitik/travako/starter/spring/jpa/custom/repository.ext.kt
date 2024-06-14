package io.arkitik.travako.starter.spring.jpa.custom

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.repository.Repository
import jakarta.persistence.EntityManager

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 9:11 PM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
inline fun <T, ID, reified R : Repository<T, ID>> createRepository(entityManager: EntityManager): R {
    val repositoryFactorySupport = JpaRepositoryFactory(entityManager)
    return repositoryFactorySupport.getRepository(R::class.java)
}
