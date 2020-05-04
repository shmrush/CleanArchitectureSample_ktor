package di

import FindUserUseCase
import FindUserUseCaseImpl
import SignInUseCase
import SignInUseCaseImpl
import SignUpUseCase
import SignUpUseCaseImpl
import UserCompletesTodoUseCase
import UserCompletesTodoUseCaseImpl
import UserCreatesTodoUseCase
import UserCreatesTodoUseCaseImpl
import UserGetsAllTodosUseCase
import UserGetsAllTodosUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<FindUserUseCase> { FindUserUseCaseImpl(get()) }
    single<SignInUseCase> { SignInUseCaseImpl(get()) }
    single<SignUpUseCase> { SignUpUseCaseImpl(get()) }
    single<UserCompletesTodoUseCase> { UserCompletesTodoUseCaseImpl(get(), get()) }
    single<UserCreatesTodoUseCase> { UserCreatesTodoUseCaseImpl(get(), get()) }
    single<UserGetsAllTodosUseCase> { UserGetsAllTodosUseCaseImpl(get(), get()) }
}