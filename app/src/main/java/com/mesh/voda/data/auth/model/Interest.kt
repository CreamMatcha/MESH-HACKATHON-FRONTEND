package com.mesh.voda.data.auth.model

/** 회원가입 시 선택하는 관심 봉사 카테고리. */
enum class Interest(val label: String) {
    ENVIRONMENT("환경·생태"),
    //EDUCATION("교육"),
    CHILDREN("아동·청소년"),
    SENIOR("노인·어르신"),
    ANIMAL("동물보호"),
    DISABILITY("장애인 지원"),
    HEALTH("의료·보건"),
    MULTICULTURE("다문화"),
    COMMUNITY("지역사회"),

    CULTURE("문화·예술"),
  //  SAFETY("재난·안전"),


    //TALENT("IT·재능기부"),

}
