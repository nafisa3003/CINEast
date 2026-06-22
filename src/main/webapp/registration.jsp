<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CINÉast — Create Account</title>
<link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
<style>
*,*::before,*::after{box-sizing:border-box;margin:0;padding:0}
:root{--bg:#0D0E12;--surface:rgba(25,28,36,0.6);--gold:#E5A93C;--text:#FFFFFF;--muted:#9EA3B0;--radius:12px}
body{min-height:100vh;background:var(--bg);color:var(--text);font-family:'Inter',sans-serif;
  display:flex;align-items:center;justify-content:center;
  background-image:radial-gradient(ellipse at 40% 60%,rgba(229,169,60,0.06) 0%,transparent 60%)}
.auth-wrap{display:flex;width:900px;max-width:96vw;min-height:580px;border-radius:20px;overflow:hidden;
  box-shadow:0 32px 80px rgba(0,0,0,0.6);border:1px solid rgba(229,169,60,0.15)}
.auth-banner{flex:1;background:linear-gradient(160deg,#1a1c25 0%,#0D0E12 100%);
  display:flex;flex-direction:column;align-items:center;justify-content:center;padding:48px;gap:24px}
.auth-banner-logo{font-family:'Bebas Neue',sans-serif;font-size:52px;letter-spacing:6px;color:#fff}
.auth-banner-logo .accent{color:var(--gold)}
.auth-banner-tagline{color:var(--muted);font-size:15px;text-align:center;line-height:1.7;max-width:240px}
.auth-form{flex:1;background:var(--surface);backdrop-filter:blur(12px);
  display:flex;flex-direction:column;justify-content:center;padding:56px 48px;gap:8px}
.auth-form h2{font-size:26px;font-weight:600;margin-bottom:8px}
.auth-form .sub{color:var(--muted);font-size:14px;margin-bottom:28px}
.auth-form .sub a{color:var(--gold);text-decoration:none}
.form-group{display:flex;flex-direction:column;gap:6px;margin-bottom:18px}
.form-group label{font-size:13px;color:var(--muted);font-weight:500;letter-spacing:.5px}
.form-group input{padding:12px 16px;background:rgba(255,255,255,0.07);border:1px solid rgba(255,255,255,0.12);
  border-radius:var(--radius);color:var(--text);font-size:15px;outline:none;
  transition:border-color .2s;font-family:'Inter',sans-serif}
.form-group input:focus{border-color:var(--gold);background:rgba(255,255,255,0.1)}
.btn-primary{width:100%;padding:14px;background:var(--gold);color:#0D0E12;
  border:none;border-radius:var(--radius);font-size:15px;font-weight:600;
  cursor:pointer;transition:opacity .2s;margin-top:8px;font-family:'Inter',sans-serif}
.btn-primary:hover{opacity:.9}
.msg-error{color:#e87070;font-size:13px;margin-bottom:12px;padding:10px 14px;
  background:rgba(232,112,112,0.12);border-radius:8px}
</style>
</head>
<body>
<div class="auth-wrap">
  <div class="auth-banner">
    <div class="auth-banner-logo">CIN<span class="accent">É</span>ast</div>
    <p class="auth-banner-tagline">Join a community of film lovers. Every movie deserves a voice.</p>
  </div>
  <div class="auth-form">
    <h2>Create your account</h2>
    <p class="sub">Already a member? <a href="login.jsp">Sign in</a></p>
    <% String error = request.getParameter("error");
       if (error != null) { %><div class="msg-error"><%=error%></div><% } %>
    <form action="userServlet" method="POST">
      <input type="hidden" name="action" value="register">
      <div class="form-group">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required autocomplete="username">
      </div>
      <div class="form-group">
        <label for="email">Email address</label>
        <input type="email" id="email" name="email" required autocomplete="email">
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required autocomplete="new-password">
      </div>
      <button type="submit" class="btn-primary">Create account</button>
    </form>
  </div>
</div>
</body>
</html>
