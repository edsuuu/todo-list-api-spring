# Entendendo a Implementação do Spring Security

Neste documento, vamos entender como o Spring Security foi configurado no seu projeto para atender à sua demanda: ter **dois tipos de comportamentos** de segurança baseados no prefixo da URL.

---

## 1. Stateful vs Stateless

### O que é Stateful?
Uma aplicação **Stateful** ("Com Estado") mantém informações sobre a sessão do usuário no servidor. 
- **Como funciona:** Quando você faz login, o servidor cria uma "Sessão" na memória (ou no banco) e devolve um cookie (geralmente chamado `JSESSIONID`) para o seu navegador. Nas próximas requisições, o navegador envia esse cookie, e o servidor sabe quem você é.
- **Uso ideal:** Aplicações Web tradicionais, onde o usuário acessa via navegador e preenche formulários HTML (Thymeleaf, JSP, JSF, etc).
- **No seu projeto:** Configuramos para que todas as rotas que **NÃO comecem com `/api`** sejam tratadas como Stateful (ex: `/list`).

### O que é Stateless?
Uma aplicação **Stateless** ("Sem Estado") não guarda nenhuma informação sobre você no servidor entre uma requisição e outra.
- **Como funciona:** O servidor não cria sessões na memória. Você precisa enviar as suas credenciais ou um Token (como JWT - JSON Web Token) **em todas as requisições** para provar quem você é.
- **Uso ideal:** APIs RESTful que são consumidas por aplicativos Mobile (Android/iOS) ou aplicações Frontend modernas (React, Angular, Vue).
- **No seu projeto:** Configuramos para que todas as rotas que **comecem com `/api/`** sejam tratadas como Stateless (ex: `/api/list`).

---

## 2. A Classe `SecurityConfig`

O coração da nossa implementação está na classe `com.edsuuu.list.config.SecurityConfig`. O Spring Security moderno funciona com **SecurityFilterChain** (Cadeia de Filtros de Segurança).

Nós criamos **duas cadeias de filtros**:

### Filtro 1: `apiFilterChain` (Stateless)
```java
@Bean
@Order(1)
public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**") // 1. Só intercepta se começar com /api/
        .csrf(AbstractHttpConfigurer::disable) // 2. Desativa proteção CSRF (necessário para APIs stateless)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 3. Define como Stateless (Não cria JSESSIONID)
        .authorizeHttpRequests(auth -> auth
            .anyRequest().authenticated()
        )
        .httpBasic(withDefaults()); // 4. Pede Usuário/Senha no Header (Basic Auth)
    
    return http.build();
}
```
**O que acontece aqui?**
- `@Order(1)`: O Spring lê os filtros em ordem. Este é o primeiro.
- `.securityMatcher("/api/**")`: Ele verifica: "A URL começa com `/api/`?". Se sim, ele aplica essas regras. Se não, ele pula para o próximo filtro.
- `.sessionCreationPolicy(SessionCreationPolicy.STATELESS)`: Garante que o Spring não crie Sessões.

### Filtro 2: `formLoginFilterChain` (Stateful)
```java
@Bean
public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable) 
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // 1. Define como Stateful
        .authorizeHttpRequests(auth -> auth
            .anyRequest().authenticated()
        )
        .formLogin(withDefaults()) // 2. Habilita tela de login do Spring
        .httpBasic(withDefaults()); 

    return http.build();
}
```
**O que acontece aqui?**
- Como não tem `@Order` explícito e nem `.securityMatcher()`, este é o filtro "padrão" (Fallback).
- Qualquer requisição que não começou com `/api/` cai aqui.
- `.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)`: Se o usuário logar, o Spring cria a sessão e manda o Cookie.
- `.formLogin()`: Se você tentar acessar `/list` no navegador sem estar logado, o Spring te redirecionará para uma página HTML bonitinha padrão de Login (`/login`).

---

## 3. O que mudou no `application.yml`?

Adicionamos estas linhas ao arquivo `application.yml`:

```yaml
  security:
    user:
      name: admin
      password: admin123
      roles: ADMIN
```
**Por que?**
Por padrão, quando você instala o Spring Security, ele gera uma senha aleatória no console toda vez que a aplicação inicia, o que é muito chato para desenvolver. Colocando essas configurações, nós fixamos o usuário como `admin` e senha `admin123`.

---

## 4. Implementação de JWT (Stateless)

Agora, os endpoints `/api/**` não usam mais Basic Auth. Eles usam **JWT (JSON Web Token)**.

### Componentes do JWT:
1.  **`JwtService`**: Responsável por criar o token (sign), extrair o usuário (extract) e validar se o token é legítimo.
2.  **`JwtAuthenticationFilter`**: Um filtro que roda antes de cada requisição para `/api/**`. Ele olha o cabeçalho `Authorization: Bearer <token>`, valida o token e diz ao Spring: "Este usuário está autenticado".
3.  **`AuthController`**: Fornece o endpoint `/api/auth/login`. É aqui que o usuário manda e-mail e senha e recebe o token de volta.
4.  **`AppConfig`**: Onde configuramos o `PasswordEncoder` (BCrypt) e o `AuthenticationManager`.

### Como usar agora:
1.  **Login**: Faça um POST para `/api/auth/login` com o JSON:
    ```json
    {
      "email": "seu-email@teste.com",
      "password": "sua-senha-encriptada"
    }
    ```
2.  **Acesso**: Pegue o `token` recebido e envie nas próximas chamadas em `/api/...` no cabeçalho:
    `Authorization: Bearer <seu_token_aqui>`

---

## 5. Observação Importante sobre Senhas

Como agora estamos usando `BCryptPasswordEncoder`, as senhas no banco de dados **precisam** estar criptografadas com BCrypt para o login funcionar. Se você inserir uma senha em texto puro no banco, o Spring não conseguirá validar.
