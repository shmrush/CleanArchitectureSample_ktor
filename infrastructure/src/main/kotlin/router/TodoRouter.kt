package router

import controller.TodosController
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import model.Session
import org.joda.time.DateTime
import org.koin.ktor.ext.inject
import respondCustomResponse

@KtorExperimentalLocationsAPI
class TodoRouterLocations {
    @Location("/api/todos")
    class Index

    @Location("/api/todos")
    data class Create(var title: String = "", var description: String = "", var deadlineAt: DateTime = DateTime.now())

    @Location("/api/todos/{id}/complete")
    data class Complete(val id: Int)
}

@KtorExperimentalLocationsAPI
fun Route.todoRouter() {
    val todosController by inject<TodosController>()

    authenticate("session-auth") {
        get<TodoRouterLocations.Index> {
            val session = call.sessions.get<Session>()
            val response = todosController.index(session)
            call.respondCustomResponse(response)
        }
        post<TodoRouterLocations.Create> {
            val params = call.receive<TodoRouterLocations.Create>()
            val session = call.sessions.get<Session>()
            val response = todosController.create(
                session,
                TodosController.CreateParams(
                    title = params.title,
                    description = params.description,
                    deadlineAt = params.deadlineAt
                )
            )
            call.respondCustomResponse(response)
        }
        put<TodoRouterLocations.Complete> {
            val session = call.sessions.get<Session>()
            val response = todosController.complete(session, it.id)
            call.respondCustomResponse(response)
        }
    }
}
