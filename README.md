# SpringSecurity


### 1. Spring Security1

1. @Configuration
  - 해당 클래스를 스프링의 설정 클래스로 지정하는 역할
  - 스프링 컨테이너가 해당 클래스를 컨포넌트 스캔하고 빈으로 등

2. View Resolver
   - 스프링 MVC에서 View를 찾아주는 역할
   - 클라이언트의 요청에 대한 응답으로 뷰를 생성하기 위해 컨트롤러에서 반환한 뷰 이름을 실제 뷰 객체로 변환하는 작업을 함

3. WebMvcConfigurer
   - Spring MVC 구성을 사용자가 직접적으로 변경하고 구성할 수 있게 해주는 인터페이즈
   - View Resolver 및 Handler Mapping 등록
   - Resource Handler 등록
   - Intercepter 등록
   - Message Converter 등록
   - View Controller 등록

4. void configureViewREsolers
   - SPring MVC에서 View Resolver를 설정하는 메서드
   - ViewResolverRegistery 객체를 사용하여 다양한 유형의 View Resolver를 추가하고 구성할 수 있
  
5. @Override configureViewResolvers

   
    <details>
      <summary>자세히</summary>
  
        public void configureViewResolvers(ViewResolverRegistry registry) {
        
          // MustacheViewResolver 객체 생성
          MustacheViewResolver resolver = new MustacheViewResolver();
          
          // 문자 인코딩 설정
          resolver.setCharset("UTF-8");
          
          // 컨텐츠 타입 설정
          resolver.setContentType("text/html;charset=UTF-8");
          
          // 뷰 파일의 위치(prefix) 설정
          resolver.setPrefix("classpath:/templates/");
          
          // 뷰 파일의 확장자(suffix) 설정
          resolver.setSuffix(".html");
          
          // 설정한 MustacheViewResolver를 뷰 리졸버 레지스트리에 등록
          registry.viewResolver(resolver);
      }
  
    </details>


6. SecurityConfig File

    <details>
      <summary>자세히</summary>

      ```
      @Configuration // IoC 빈(bean)을 등록
      @EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록
      public class SecurityConfig {
          /*
          기존: WebSecurityConfigurerAdapter를 상속하고 configure매소드를 오버라이딩하여 설정하는 방법
          현재: SecurityFilterChain을 리턴하는 메소드를 빈에 등록하는 방식(컴포넌트 방식으로 컨테이너가 관리)
          //https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
              http.csrf().disable();
              http.authorizeRequests()
                      .antMatchers("/user/**").authenticated()
                      .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                      .antMatchers("/admin").access("\"hasRole('ROLE_ADMIN')")
                      .anyRequest().permitAll();
          }
        */
        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {
            // http.csrf((csrf) -> csrf.disable());
            
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorize ->
                            authorize
                                    .requestMatchers("/user/**").authenticated()
                                    // "/user/**"로 시작하는 요청은 인증이 필요합니다
                                    .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                    // "/manager/**"로 시작하는 요청은 "ADMIN" 또는 "MANAGER" 역할이 필요합니다
                                    .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                    // "/admin/**"로 시작하는 요청은 "ADMIN" 역할이 필요합니다
                                    .anyRequest().permitAll() // 다른 모든 요청은 인증 없이 허용됩니다
                    )
                    .formLogin(login ->
                            login
                                    .loginPage("/login") // 사용자 지정 로그인 페이지 URL 설정
                                    //.defaultSuccessUrl("/view/dashboard", true) // 성공 시 대시보드로 이동
                                    //.permitAll() // 로그인 페이지는 모든 사용자에게 허용됩니다
                    );
            return http.build(); // 구성된 SecurityFilterChain 반환
        }
    
    
    }
      ```


    </details>


### 2. Oauth 
1. OAuth -> open Auth : 인증 처리를 대신한다.
2. 인증 : CallBack code 받는 것
3. 권한 : Access Token 받는 것
4. Resource owner : 사용자
5. client : 사업 페이지 -> OAuth-client Lib 는 facebook. google 만 채택
6. OAuth(인증) Server : API 서버 (포탈)
7. Resource Server : 자원 서버
