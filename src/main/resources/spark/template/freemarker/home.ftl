<!DOCTYPE html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>

<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <#if currentUser??>
      <h2>Online Users (${playersOnline} online): <br></h2>

      <!-- <input type="checkbox", id=isSpectator, name="spectatorBox", value="True">
      <label for "spectatorBox">Join as Spectator</label> <br> -->

      <div class="lists">
        <div id="players">
          <h3>Challenge Players:</h3>
          <#if availablePlayers?size - 1 == 0>
            <div>There are no available players :(</div>
          </#if>

          <#list availablePlayers as player>
            <#if !(player == currentUser)>
              <form action="./startGame" method="POST">
                <input type="hidden" name="otherUser" value="${player.name}" />
                <button type="submit"> ${player.name} </button>
              </form>
            </#if>
          </#list>
        </div>

        <div id="games">
          <h3>Spectate Players:</h3>
          <#if playersInGame?size== 0>
            <div>There are no players to spectate :(</div>
          </#if>

          <#list playersInGame as playerBeingSpectated>
            <form action="/spectator/game" method="POST">
              <input type="hidden" name="otherUser" value="${playerBeingSpectated.name}" />
              <button type = "submit">${playerBeingSpectated.name}</button>
            </form>
          </#list>
        </div>
      </div>

    <#else>
      <h2> There are currently ${playersOnline} online. Sign in to play a match!</h2>

    </#if>
    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->
  </div>

</div>

</body>

</html>