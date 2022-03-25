![header](https://capsule-render.vercel.app/api?type=cylinder&color=auto&customColorList=19&text=MINE&fontAlignY=45&fontSize=40&height=150&animation=twinkling&desc=Discover👀%20|%20Bid💸%20|%20Mine🎁&descAlignY=70)

## About
* Mine은 경매 서비스를 제공하기 위한 프로젝트입니다.
* C2C 거래 형태를 지원합니다.
* 상품을 구매하기 위해서는 입찰을 하고 낙찰되어야 합니다. 만약 판매자가 즉시 구매가를 설정했다면 즉시 구매도 가능합니다.
* 자동 입찰을 설정할 수 있습니다. 지불할 의사가 있는 최대 금액을 입력하면 사용자를 대신하여 증분 입찰가로 응찰합니다.

## System Design
![Mine System Design](https://user-images.githubusercontent.com/76784643/160184529-86aa0974-fff9-4e27-a27d-48c3055e6995.png)

## Issues
* 경매 특성상 희소성 있는 상품일수록 수많은 사용자들이 경매에 참여하게 됩니다. 이러한 상황에서 발생하는 대규모 트래픽에 대응하기 위해서 Scale Out이 필요합니다.  
[issue 1](https://www.notion.so/leeseowoo/Scale-Out-9cc85703f1b74176a5fe644898c5bc05)

* 입찰 프로세스를 진행하는 중에 발생할 수 있는 문제를 파악하고 해결책을 고안하기 위해서 발생 가능한 모든 경우의 입찰 프로세스를 정리했습니다.  
[issue 2](https://www.notion.so/leeseowoo/90e60d1c1258420f876f66347fbc719f)

* Scale Out된 서버들에 입찰 요청이 동시에 분산되어 전달됐을 때 발생하는 Concurrency Issue는 단일 지점에서 해결해야 합니다. 문제를 해결하기 위해서 DB Lock이 필요합니다.  
[issue 3](https://www.notion.so/leeseowoo/Scale-Out-Concurrency-Issue-DB-Loc-1de5ac1eb231455c9bafcad0ae849cca)

* 수동/자동 입찰을 처리하는 서버를 통일하는 구조와 분리하는 구조의 장단점을 생각해 봤습니다. 통일된 구조를 채택했으며 단점(자동 입찰 조회 처리 시간)을 보안하기 위해서 Redis가 필요합니다.  
[issue 4](https://www.notion.so/leeseowoo/Redis-4fd9dbfaa5d24988a5a071d8ee38d9a0)
