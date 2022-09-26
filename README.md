![header](https://capsule-render.vercel.app/api?type=cylinder&color=auto&customColorList=19&text=MINE&fontAlignY=45&fontSize=40&height=150&animation=twinkling&desc=Discover👀%20|%20Bid💸%20|%20Mine🎁&descAlignY=70)

## About
* Mine은 경매 서비스를 제공하기 위한 프로젝트입니다.
* C2C 거래 형태를 지원합니다.
* 상품을 구매하기 위해서는 입찰을 하고 낙찰되어야 합니다. 만약 판매자가 즉시 구매가를 설정했다면 즉시 구매도 가능합니다.
* 자동 입찰을 설정할 수 있습니다. 지불할 의사가 있는 최대 금액을 입력하면 사용자를 대신하여 증분 입찰가로 응찰합니다.

## Prototype
카카오 오븐을 사용해서 제작한 화면 프로토타입입니다.  
👉 [**More Info**](https://ovenapp.io/view/k2rFY8UlHtr6riDPqkIHbIMFPgvRjml7/)  
![Prototype](https://user-images.githubusercontent.com/76784643/192091701-58e36ee7-b121-49dd-9f85-34a5ee9fd796.png)

## System Design
![Mine System Design_V2](https://user-images.githubusercontent.com/76784643/192092203-663cd4ae-93bb-4178-8dc3-02ac40220633.png)

## DDD Layered Architecture
DDD Layered Architecture를 적용했습니다. 각 계층의 역할, 계층 간 참조 관계, 각 계층에서 사용하는 클래스 네이밍, 클래스 역할에 관한 설명은 프로젝트 👉[**Wiki**](https://github.com/f-lab-edu/mine/wiki/2.-DDD-Layered-Architecture)에서 확인할 수 있습니다.

## ERD
각 테이블의 역할, 컬럼, 테이블 간 참조 관계에 관한 설명은 프로젝트 👉[**Wiki**](https://github.com/f-lab-edu/mine/wiki/6.-ERD)에서 확인할 수 있습니다.

## Issues
* 희소성 있고 경매 시작가가 낮은 경매일수록 수많은 사용자들이 경매에 참여할 텐데, 이런 상황에서 발생하는 대규모 트래픽에 어떻게 대응할까?
* Scale Out된 서버들에 수많은 입찰 요청이 동시에 전달될 때 발생하는 Concurrency Issue를 어떻게 해결할까?
* 최고 입찰가 변경을 위해 경매 테이블의 Row에 Exclusive Lock을 설정하면 '읽기', '잠금 설정', '쓰기', '삭제'가 불가능한데, 경매 생성자가 원할 때 경매 타이틀 수정 등의 작업을 못하진 않을까?
* 최고 입찰가 테이블의 Row에 Exclusive Lock을 설정하면, 카탈로그 조회 시 필요한 최고 입찰가 데이터를 못 가져와서 사용자가 원할 때 카탈로그 조회가 불가능하진 않을까?
* 매 입찰 요청마다, 자동 입찰 등록 유무 확인을 위한 동일한 읽기 오퍼레이션을 어떻게 효율적으로 처리할 수 있을까?
* 10,001 원, 10,002 원, 10,003 원 ... 1 원 단위로 입찰이 가능할 경우 어떤 문제가 발생할 수 있을까?
* 입찰 증분 계산을 위해 생각한 5가지 방법 중 어떤 방법의 성능이 가장 우수할까?
* 경매 마감 시간에 다다른 시점, 잠재 입찰자의 Access Token이 만료되면 경매 생성자와 기업 측이 최대 이익을 갖게 할 기회가 상실될 텐데, Refresh Token을 사용하지 않고도 Access Token을 발급할 수 있는 방법이 없을까?

## Terms
Term | Description
---- | ----
입찰 | 상품 구매 희망 가격을 제출하여 경매에 참여하는 일
응찰 | 입찰에 응함
낙찰 | 경매 종료 시점에서 최고 입찰자에게 상품 구매 권한이 주어지는 일
패찰 | 낙찰에 실패함
유찰 | 경매 참여자가 없는 등의 이유로 낙찰이 결정되지 않고 무효되는 일
입찰 증분 | 다음 입찰을 위한 입찰 가능한 최소 금액을 산정하기 위해 최고 입찰가에 더해지는 일정 금액
