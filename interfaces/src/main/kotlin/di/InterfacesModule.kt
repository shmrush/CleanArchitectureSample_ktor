package di

import controller.TodosController
import controller.UsersController
import database.repository.TodoDaoRepositoryImpl
import database.repository.UserDaoRepositoryImpl
import repository.TodoDaoRepository
import repository.UserDaoRepository
import org.koin.dsl.module

val interfacesModule = module {
    // Controller
    single<UsersController> { UsersController(get(), get()) }
    single<TodosController> { TodosController(get(), get(), get()) }

    // Repository
    single<UserDaoRepository> { UserDaoRepositoryImpl() }
    single<TodoDaoRepository> { TodoDaoRepositoryImpl() }
}