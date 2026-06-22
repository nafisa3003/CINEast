<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%
    String username = null;
    if (session != null) username = (String) session.getAttribute("username");
%>

<nav class="cineast-nav">
  <a class="nav-logo" href="MovieServlet">
    <span class="nav-brand">CIN<span class="accent">É</span><span class="logo-lowercase">ast</span></span>
  </a>
</nav>
  <form class="nav-search" action="MovieServlet" method="GET">
    <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/>
    </svg>
    <input type="text" name="search" placeholder="Search movies…" class="search-input">
  </form>
  <div class="nav-right">
    <% if (username != null) { %>
      <span class="nav-username">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="width:16px;height:16px;vertical-align:middle;margin-right:6px;">
          <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
        </svg>
        <%= username %>
      </span>
      <form action="userServlet" method="post" style="display:inline">
        <input type="hidden" name="action" value="logout">
        <button type="submit" class="btn-logout">Sign out</button>
      </form>
    <% } else { %>
      <a href="login.jsp" class="btn-logout">Sign in</a>
    <% } %>
  </div>
</nav>
<style>
:root {
  --bg: #0D0E12;
  --surface: rgba(25,28,36,0.6);
  --gold: #E5A93C;
  --text: #FFFFFF;
  --muted: #9EA3B0;
  --radius: 8px;
}
.cineast-nav {
  position: sticky; top: 0; z-index: 100;
  display: flex; align-items: center; gap: 24px;
  padding: 0 32px; height: 64px;
  background: rgba(13,14,18,0.92);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(229,169,60,0.15);
}
.nav-logo { display:flex; align-items:center; gap:10px; text-decoration:none; }
.nav-logo-img { height:32px; width:auto; }
.nav-brand { font-family:'Bebas Neue',sans-serif; font-size:24px; letter-spacing:3px; color:var(--text); }
.accent { color:var(--gold); }
.logo-lowercase {
  font-family: 'Inter', sans-serif;
  font-weight: 700;
  text-transform: lowercase;
  letter-spacing: 1px;
}
.nav-search { flex:1; max-width:480px; position:relative; }
.search-icon { position:absolute; left:14px; top:50%; transform:translateY(-50%); width:17px; height:17px; color:var(--muted); }
.search-input {
  width:100%; padding:10px 16px 10px 42px;
  background:rgba(255,255,255,0.07); border:1px solid rgba(255,255,255,0.12);
  border-radius: 24px; color:var(--text); font-size:14px; outline:none;
  transition: border-color .2s, background .2s;
}
.search-input::placeholder { color:var(--muted); }
.search-input:focus { border-color:var(--gold); background:rgba(255,255,255,0.1); }
.nav-right { display:flex; align-items:center; gap:16px; margin-left:auto; }
.nav-username { color:var(--muted); font-size:14px; }
.btn-logout {
  padding:7px 18px; background:transparent; border:1px solid var(--gold);
  color:var(--gold); border-radius:var(--radius); font-size:13px;
  cursor:pointer; transition: background .2s, color .2s; text-decoration:none;
}
.btn-logout:hover { background:var(--gold); color:#0D0E12; }
</style>
