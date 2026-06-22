<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="patterns.TMDbMovieAdapter.Movie, java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Search — CINÉast</title>
<link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
<style>
*,*::before,*::after{box-sizing:border-box;margin:0;padding:0}
:root{--bg:#0D0E12;--surface:rgba(25,28,36,0.6);--gold:#E5A93C;--text:#FFFFFF;--muted:#9EA3B0;--radius:8px}
html,body{background:var(--bg);color:var(--text);font-family:'Inter',sans-serif;min-height:100vh}
a{text-decoration:none;color:inherit}
.page-header{padding:40px 40px 0;margin-bottom:32px}
.page-header h1{font-family:'Bebas Neue',sans-serif;font-size:32px;letter-spacing:2px}
.page-header h1 .accent{color:var(--gold)}
.result-count{color:var(--muted);font-size:14px;margin-top:6px}
.results-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(160px,1fr));
  gap:20px;padding:0 40px 80px}
.movie-card{position:relative;border-radius:var(--radius);overflow:hidden;cursor:pointer;
  transition:transform .25s,box-shadow .25s}
.movie-card:hover{transform:translateY(-6px);box-shadow:0 20px 48px rgba(0,0,0,0.6)}
.movie-card img{width:100%;aspect-ratio:2/3;object-fit:cover;display:block}
.card-overlay{position:absolute;inset:0;background:linear-gradient(to top,rgba(13,14,18,0.95) 0%,rgba(13,14,18,0.2) 55%,transparent 100%);
  opacity:0;transition:opacity .25s;display:flex;flex-direction:column;justify-content:flex-end;padding:14px}
.movie-card:hover .card-overlay{opacity:1}
.card-title{font-size:13px;font-weight:600;line-height:1.3;margin-bottom:6px;
  display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden}
.card-meta{display:flex;gap:8px;align-items:center}
.card-rating{color:var(--gold);font-size:12px;font-weight:600}
.card-year{color:var(--muted);font-size:11px}
.card-no-poster{width:100%;aspect-ratio:2/3;background:#1a1c25;display:flex;align-items:center;justify-content:center;color:var(--muted);font-size:12px}
.no-results{text-align:center;padding:80px 40px;color:var(--muted)}
.no-results h2{font-family:'Bebas Neue',sans-serif;font-size:28px;letter-spacing:2px;margin-bottom:8px;color:var(--text)}
</style>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<%
  List<Movie> results = (List<Movie>) request.getAttribute("searchResults");
  String query = (String) request.getAttribute("searchQuery");
  if (query == null) query = request.getParameter("search");
  if (query == null) query = "";
%>
<div class="page-header">
  <h1>Results for <span class="accent">"<%= query %>"</span></h1>
  <p class="result-count"><%= (results != null ? results.size() : 0) %> movie<%= (results == null || results.size() != 1) ? "s" : "" %> found</p>
</div>
<% if (results == null || results.isEmpty()) { %>
<div class="no-results">
  <h2>No results found</h2>
  <p>Try a different search term or browse trending movies.</p>
</div>
<% } else { %>
<div class="results-grid">
  <% for (Movie m : results) { %>
  <a href="MovieServlet?movieId=<%= m.getId() %>" class="movie-card">
    <% if (m.getPosterPath() != null && !m.getPosterPath().isEmpty()) { %>
    <img src="<%= m.getPosterUrl() %>" alt="<%= m.getTitle() %>" loading="lazy">
    <% } else { %><div class="card-no-poster">No image</div><% } %>
    <div class="card-overlay">
      <div class="card-title"><%= m.getTitle() %></div>
      <div class="card-meta">
        <span class="card-rating">★ <%= m.getRatingFormatted() %></span>
        <span class="card-year"><%= m.getReleaseYear() %></span>
      </div>
    </div>
  </a>
  <% } %>
</div>
<% } %>
<jsp:include page="footer.jsp"/>
</body>
</html>
