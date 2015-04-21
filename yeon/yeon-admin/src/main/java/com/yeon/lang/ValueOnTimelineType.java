package com.yeon.lang;

public enum ValueOnTimelineType {
	CONSTANT // 시간에 영향을 받지 않고 항상 동일한 값
	, VOLATILE // 생성된 이후 특정 시간이 지나면 의미가 없는 값.
	, SPOT // 특정 시점의 상태를 반영하는 값. (snapshot?)
	, AVATAR // 특정 기간 동안에만 나옴. 새로운 값이 생성될 때까지는 상수
	, ALIAS // 별명.
	;
}
