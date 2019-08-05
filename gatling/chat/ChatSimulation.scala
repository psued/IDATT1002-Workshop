package chat

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ChatSimulation extends Simulation {

	val httpProtocol = http
    .baseURL("http://localhost")
		.inferHtmlResources(BlackList(""".*\.css""", """.*\.js""", """.*\.ico"""), WhiteList())
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("no,en;q=0.9,en-US;q=0.8,nb-NO;q=0.7,nb;q=0.6")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36")

	val headers_0 = Map(
		"Content-Type" -> "application/json; charset=utf-8;",
		"Origin" -> "http://localhost"
              )

	val headers_3 = Map(
		"Accept" -> "application/json",
		"Content-Type" -> "application/json",
		"Origin" -> "http://localhost"
              )

        val uri1 = "http://localhost/api"

        private val userFeeder = csv("usernames.csv").random

	val scn = scenario("ChatSimulation")
                .feed(userFeeder)
		.exec(http("Login")
			.post("/api/user")
			.headers(headers_0)
			.body(StringBody(
                          """{
                            | "username": "${username}",
                            | "password": "passordpassord"
                            |}""".stripMargin))
                        .resources(http("Fetch users")
			.get("/api/user/"))
                        .check(
                          jsonPath("$[?(@.username=='${username}')].userId").findAll.saveAs("userId")
                        ))
                .pause(6)
		.exec(http("Click on Ola")
			.get("/api/message/${userId(0)}/1"))
		.pause(16)
		.exec(http("Send msg to Ola")
			.post("/api/message")
			.headers(headers_3)
                        .body(StringBody(
                          """{
                            | "userId1": ${userId(0)},
                            | "userId2": 1,
                            | "messageContent":"Hei fra gatling"
                            |}""".stripMargin)))
		.pause(15)
		.exec(http("Click on kari")
			.get("/api/message/${userId(0)}/2"))
		.pause(2)
		.exec(http("Send msg to Kari")
			.post("/api/message")
			.headers(headers_3)
                        .body(StringBody(
                          """{
                            | "userId1": ${userId(0)},
                            | "userId2": 2,
                            | "messageContent":"Hei fra gatling"
                            |}""".stripMargin)))

	setUp(scn.inject(atOnceUsers(100))).protocols(httpProtocol)
}
