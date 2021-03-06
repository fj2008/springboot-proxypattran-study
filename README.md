## 프록시 패턴 데코레이터 패턴

- springMVC는 @Controller 또는 @ReqeustMapping 애노테이션이타입이 있어야 스프링 컨트롤러로 인식한다. 이 어노테이션은 인터페이스에사용해도 된다.
- @Import(AppV1Config.class) 는 클래스를 스프링빈으로 등록하는것입니다.
    - 일반적으로 @configration같은 설정파일을 등록할때 사용하지만 일반적으로 스프링빈으로 띄울때 사용하기도 한다.

### @SpringBootApplication(scanBasePackages = "hello.proxy.app")
위 어노테이션은 컨포넌트 스켄 대상을 조정하는것이다. app하위파일만 컨포넌트스켄을 하는것이다 이렇게 한이유 
@import를 해서 Config패키지를 컨포넌트대상이 되지 않도록 하기위해서 app하위 파일만 컨포넌트 스켄대상이 되도록 설정했다.

## v2 인터페이스가 없는 구체클래스를 스프링빈 등록
rueqestmapping은 컨포넌트 대상이 안된다.
수동으로 빈등록을목표로 하고있기때문에 RequestMapping을 사용

## v3 는 자동으로 컨포넌트스켄대상이 돼서 스프링빈에 자동으로 등록되도록 예제 생성

# 요구사항 추가
결과적으로 어떠한 기능을 추가되거나 변경되면 기존 원본코드를 변경해야한다는 단점이 분명하게 있다.

- 원본 코드를 전혀 수정하지 않고 로그추적기 적용
- 특정 메서드는 로그를 출력하지 않는 기능
  - 보안상 일부는 로그를 출력하면 안된다.
- 다음과 같은 다양한 케이스에 적용할 수 있어야한다.
- 인터페이스가 있는 구현클래스 v1
- 인터페이스가 없는 구체클래스 v2
- 컴포넌트 스캔대상에 기능 적용 v3

*위문제를 해결하기 위해서는 프록시 패턴을 이해해야한다*


# 프록시, 프록시패턴, 데코레이터 패턴

## 클라이언트와 서버
클라이언트와 서버라고 하면 개발자들은 보통 서버 컴퓨터를 생각한다.
사실 클라이언트와 서버의 개념은 상당히 넓게 사용된다. 클라이언트는 의뢰인이고 서버는 서비스나 상품을 제공하는 사람이나 물건을 뜻한다.
클라이언트는 서버에 필요한것을 요청하고 , 서버는 클라이언트의 요청을 처리하는 것이다.

### 직접호출과 간접호출
직접호출은 클라이언트가 곧바로 서버에게 요청하는것.
간접호출은 클라이언트가 서버에 곧바로 요청하는것이아니라 어떠한 대리자를 통해서 서버에 요청을 하는것이다.
이 대리자를 proxy라고 한다.
**이 프록시의 장점은 중간에 대리자가 많은 일을 할 수 있다는것**

### 프록시 체인
대리자가 서버에 가기전에 또다른 대리자를 부를 수도 있다. 클라이언트는 요청하는 데이터를 받기만 하면 되는것이다.

### 객체에서의 프록시의 역할
1. 접근재어
- 권한에 따른 접근 차단
- 캐싱
- 지연로딩(실제 서버에 요청이 왔을때 로딩을 하는등등..)
3. 부가기능 추가
- 원래 서버가 제공하는 기능에 더해서 부가기능을 수행한다.
- 요청값이나 응답값을 중간에 변형한다.
- 실행 시간을 측정해서 추가 로그를 남긴다.
- 객체에서 프록시의 역할
1. 객체에서 프록시가 되려면 클라이언트는 서버에 요청한것인지 프록시에 요청한것인지 조차 몰라야한다.
2. 쉽게 이야기 해서 서버와 프록시는 같은 인터페이스를 사용해야한다. 그리고 클라이언트가 사용하는 서버객체를 프록시 객체로 변경해도 클라이언트 코드를 
변경하지 않고 동작할 수 있다.
3. **DI해서 대체가능해야한다는 뜻.**

## 프록시 패턴과 데코레이터 패턴
프록시와 프록시패턴은 다른 뜻이다.
프록시 패턴 : 접근 제어가 목적
데코레이터 페턴 : 새로운 기능 추가가 목적


## 프록시 패턴 test proxy.code설명
private Subject target : 클라이언트가 프록시를 호출하면 프록시가 최종적으로 실제 객체를 호출해야한다. 따라서 내부에 실제 객체의 참조를 가지고 있어야한다.
이렇게 프록시가 호출하는 대상을 'target'이라고 한다.
operation() : 구현한 코드를 보면 최초에 cachevalue가 null이면 실제객체에 호출을 하고 이후 호출될때는 cachevalue의 값이 바로 호출되도록 돼있다.
그래서 처음 조회 이후의 조회는 배우 빠르게 데이터를 들 고 올 수 있다.
결과적으로 프록시 패턴을 도입해서 첫요청이후의 요청은 바로바로 응답할 수 있게 됬다.

- 프록시 패턴의 핵심은 로직을 전혀 변경하지 않고 프록시를 도입해서 접근 제어를 했다라는 점이다. 클라이언트 코드의 변경없이 자유롭게 프록시를 넣고 뺄 수 있다.

## 데코레이터 페턴
데코레이터페턴의 특징은 기능을 추가할때 꾸며주는 대상이 있어야한다. 따라서 내부에 호출대상을 가지고 있어야하고 그 대상을 항상 호출하여야 하는데 이는 중복이다.
이 중복을 없애기 위해서 추상클래스방법도 있다.이렇게 하면 어떤게 컴포넌트인지 데코레이터인지 명확하게 구분할 수 있다.


## 위 두가지 패턴을 구분하는 방법은 intent(의도)를 구분하는것이다.
프록시패턴과 데코레이터 패턴은 그 모양이 거의 같고 상황에 따라 정말 똑같을 수도 있지만
디자인패턴에서 중요한것은 해당 패턴 겉모양이 아니라 그패턴을 만든 의도가 더 중요하다.


## 클래스기반 프록시의 단점
super(null) : 자바의 기본문법때문에 넣어야 하지만 프록시는 부모 객체 기능을 넣지 않기때문에 null을 넣었다.

## 인터페이스 기반 프록시와 클래스 기반 프록시

### 인터페이스기반 프록시 vs 클래스 기반 프록시
- 인터페이스가 없어도 클래스 기반으로 프록시를 생성할 수 있다.
- 클래스 기반 프록시는 해당 클래스에만 적용할 수 있다. 인터페이스 기반 프록시는 인터페이스만 같으면 모든 곳에 적욕할 수 있다ㅓ.
- 클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.
  - 부모 클래스의 생성자를 호출해야한다.
  - 클래스에 final 키워드가 붙으면 상속이 불가능하다.
  - 메서드에 final 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다

이렇게 보면 인터페이스기반의 프록시가 더 좋아보인다. 맞다. 인터페이스 기반의 프록시는 상속이라는 제약에서 자유롭다.
프로그래밍 관점에서도 인터페이스를 사용하는 것이 역할과 구현을명확하게 나누기 때문에 더 좋다.
인터페이스 기반 프록시의 단점은 인터페이스가 필요하다는 그 자체가 단점이다. 인터페이스가 없으면 인터페이스기반 프록시를 만들 수 없다.

이론적으로는 모든 객체에 인터페이스를 도입해서 역할과 구현을 나누는 것이 좋다. 이허게 하면 역할과 구현을 나누어서 구현체를 매우 편리하게 변경할 수 있다.
인터페이스를 도입하는것은 구현을 변경할 가능성이 있을때 효과적인데 구현을 변경할 가능성이 거의 없는 코드에 무작정 인터페이스를 사용하는것은 번거롭고
그렇게 실용적이지 않다.
핵심은 인터페이스가 항상 필요하지 않다는 것이다.

# 결론
실무에서는 프록시를 적용할때 v1처럼 인터페이스가 있는 경우도 있고, v2처럼 구체클래스가 있는 경우도 있다. 둘이함께 섞여있기때문에 두가지 상황에 대응할 수 있어야한다.


**너무많은 프록시 클래스**

프록시의장점은 기존코드를 변경하지않고 로그추적기에 부가기능을 추가할 수 있다는것인데
문제는 프록시클래스를 너무 많이 만들어야 한다는 점이다. 만약에 적용해야하는 대상 클래스가 100개라면 프록시도 100개라는 뜻.
프록시 클래스를 하나만 만들어서 모든 곳에 적용하는 방법은 없을까?
그 방법이 바로 동적 프록시이다.


# 리플랙션
자바가 기본으로 제공하는 JDK동적  프록시 기술이나 CGLIB같은 프록시 생성 오픈소스 기술을활용하면
프록시 객체를 동적으로 만들어 낼 수 있다.
리플랙션 기술을 사용하면 클래스나 메서드의 메타정보를 동적으로 획등하고 코드도 동적으로 호출 할 수 있다.
 정적인 코드를 리플랙션을 사용해서 Method라는 메타정보로 추상화한후 공통로직을 만들 수 있게 된다.

## 주의
리플렉션을 사용하면 클래스와 메서드의 메타정보를 사용해서 애플리케이션을 동적으로 유연하게 만들수 있다 하지만 
리플렉션 기술은 런타임에 동작하기 때문에 컴파일시에 오류를 잡을 수 없다.
 리플랙션은 프레임워크 개발이나 또는 매우 일반적인 공통처리가 필요할때 부분적으로 주의해서 사용한다.

# JDK 동적 프록시
프록시패턴을 적용하기 위해서는 저거용대상의 숫자만큼 많은 프록시 클래스를 만들어야 했다.
프록시의 로직은 같은데 적용 대상만 차이가 있는것이다.

이 문제를 해결해 주는 것이 동적 프록시 기술이다.
동적 프록시 기술을 사용하면 개발자가 직접 프록시 클래스를 만들지 않아도 된다. 이름 그대로 프록시 객체를 동적으로
런타임에 개발자 대신만들어준다. 그리고 동적 프록시에 원하는 실행로직을 지정할 수 있다.
 **JDK동적 프록시는 인터페이스를 기반으로 프록시를 동적으로 만들어준다. 따라서 인터페이스가 필수이다.**

### JDK 동적 프록시 InvocationHandler
JDK 동적 프록시에 적용할 로직은 InvocationHandler 인터페이스를 구현해서 작성해야한다.
인수들은 다음과같다
- Object proxy: 프록시 자신
- Method method: 호출한 메서드
- Object[] args : 메서드를 호출할 때 전달한 인이렇
Proxy.newProxyInstance(로드할 클래스,어떤인터페이스기반으로 프록시사용할지, 프록시가 사용해야하는 로직)
위와같이 세개의 인수를 넣어준다.
- 해당 결과로 받은 프록시 객체를 빈으로 등록을 해서 사용할 수있다.
- JDK동적프록시는 인터페이스가 필수이기때문에 인터페이스가 없을땐 사용할 수 없다.
- 그럴때 사용하는 라이브러리가 CGLIB이다.

## CGLIB
- CGLIB : Code Generator Library
- 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공하는 라이브러리이다.
- 인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 만들어낼 수 있다.
- 원래 외부라리브러리인데 스프링에서 자체적으로 가지고 있다.
  ConcreteService target = new ConcreteService();
```

ConcreteService target = new ConcreteService();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class);
        enhancer.setCallback(new TimeMethodInterceptor(target));
ConcreteService proxy = (ConcreteService)enhancer.create();
```
- Enhancer : cglib는 해당 Enhancer를 사용해서 프록시를 생성한다.
- enhancer.setSuperclass(ConcreteService.class) : 구체 클래스를 상속 받아서 프록시를 생성할 수 있다. 어떤 쿠체클래스를 
상속받을지 지정한다.
- enhancer.setCallback(new TimeMethodInterceptor(target)): 프록시에 적용할 실행 로직을 할당한다.
- enhancer.create(): 프록시를 생성한다. 압서 설정한 setSuperclass에서 지정한 클래스를 상속 받아서 프록시가 만들어진다.

### CGLIB제약
- 클래스 기반 프록시는 상속을사용하기 때문에 몇가지 제약이 있다.
  - 부모클래스의 생성자를 체크해야한다. CGLIB는 자식 클래스를 동적으로 생성하기때문에 기본 생성자가 필요한다.
  - 클래스에 final키워드가 붙으면 상속이 불가능하다. 예외발생
  - 메서드에 final키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다.
  

# 프록시 팩토리(스프링이 지원하는 프록시)

동적 프록시의 문제점
- 인터페이스가 있는경우에는 JDK동적 프록시를 적용하고 그렇지 않은 경우에 CGLIB를 적용하려면 어떻게 해야할까?
- 동시에 사용하는 경우에는 중복으로 만들어서 관리해야하나?
- 특정 조건에 맞을때 프록시 로직을 적용하는 기능도 공통으로 제공되었으면

## 프록시 팩토리
스프링은 동적 프록시를 통합해서 편리하게 만들어주는 프록시 팩토리 라는 기능을 제공한다.
프록시 팩토리는 인터페이스가 있으면 JDK동적프록시를 사용하고 구체 클래스만 있으면 CGLIB를 지원한다.

클라이언트가 프록시팩토리로 요청을 하면 프록시 팩토리가 상황에 따라서 jdk동적프록시나 CGLIB구체클래스 프록시로 반환해준다.

- 부가기능을 적용하기위해 각각 중복으로 따로 만들어야 할까?
스프링은 이문제를 해결하기 위해 부가 기능을 적용할때 'Advice'라는 새로운 개념을 도입했다.
개발자는 'Advice'만 만들면 InvocationHandler나 MethodInterceptor를 신경쓰지않아도 된다.
- 특정 조건에 맞을때 프록시 로직을 적용하는 기능도 공통으로 제공된다.이때 스프링에선 'Pointcut'을 사용하면된다.