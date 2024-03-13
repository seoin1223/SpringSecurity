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
