package com.ll;

public class Calc {
  public static int run(String exp) {
    exp = exp.trim();//입력받은 문자의 앞뒤 공백을 제거 혹시라도 100, (100)처럼 연산식 없이 문자열만 들어올 수도 있기에
    exp = stripOuterBracket(exp);//스트립아웃브라켓 입력받은 문자에 괄호를 제거해주려고 만든 메서드
    //(100) ((200)) 처럼 괄호에 쌓여서 올수 있기 떄문에

    // 연산기호가 없으면 바로 리턴
    if (!exp.contains(" ")) return Integer.parseInt(exp);//입력받은 문자열에 연산기호가 없다면 그 자체로 그 숫자이기 때문에
    //숫자 자체만 반환해주려고
    //의 3단계를 거치면 숫자 그자체만 입력됫을 경우 바로 해결해 줄수 있기 때문에
    //아래는 숫자와 연산기호도 포함 됐을 때를 대비

    boolean needToMultiply = exp.contains(" * ");//문자열에 * 가 있는 상황을 찾아서 조건식에 사용하기 위해
    //문자열에 포함되어 있지 않을 때의 상황을 걸러내기 위해
    boolean needToPlus = exp.contains(" + ") || exp.contains(" - ");
    //더하기 하는 상황을 고려햇을때 exp,contains() 입력받은 문자열에 +가 있는지를 판별하고 contains는 true false 반환하기 때문에
    //그 값을 변수에 저장
    //플러스에 마이너스 기능을 합치려고 or을 사용해서 문자열에 -가 있는 상황도 가능하게 바꿔준 것
    //그에따라 플러스나 마이너스 둘중 하나라도 있으면 플러스로 계산하려고

    boolean needToCompound = needToMultiply && needToPlus;//다항식이 입력받았을때 곱하기와 더하기를 포함하고 있는경우
    boolean needToSplit = exp.contains("(") || exp.contains(")");//문자열에 괄호가 하나라도 포함되어 있는 경우

    if (needToSplit) {//괄호를 포함하고 있을때는 실행 그렇지 않으면 실행하지 안흠

      int splitPointIndex = findSplitPointIndex(exp);

      String firstExp = exp.substring(0, splitPointIndex);
      String secondExp = exp.substring(splitPointIndex + 1);

      char operator = exp.charAt(splitPointIndex);

      exp = Calc.run(firstExp) + " " + operator + " " + Calc.run(secondExp);

      return Calc.run(exp);

    } else if (needToCompound) {//는 곱하기와 그리고 더하기를 포함하고 있을때 and의 상황 둘다 참일때 문자열에 곱셈 덧셈이
      //있는 경우에만 실행하기 위해서
      String[] bits = exp.split(" \\+ ");
      //입력받은 다항식을 +로 나누어 준다 이유는 곱셈과 덧셈이 괄호없이 존재한다면 곱셈먼저가 실행되기 때문에
      //예를 들어 1 + 2 * 3 이라면  1 과 2 * 3 으로 나뉜다

      return Integer.parseInt(bits[0]) + Calc.run(bits[1]); // TODO
    }
    if (needToPlus) {//문자열에 더하기가 있다고 판별하는 조건식 더하기가 있을때
      exp = exp.replaceAll("\\- ", "\\+ \\-");
      //입력받은 문자열을 치환하는 행위 needtoplus에 -일때의 경우도 생각해서 -를 + - 로 바꿔준다 그러면
      //빼기는 음수를 더해주는 행위로 볼수 있으므로 -가 있다면 + - 로 바꿔서 -를 정수에 붙여서 더하기의 행위로 만들어주려고

      String[] bits = exp.split(" \\+ ");
      //문자열컨트롤할 bits에 입력받은 exp를 +를 기준으로 나눈다

      int sum = 0;
      //합을 저장할 임의의 공간 sum을 만든다

      for (int i = 0; i < bits.length; i++) {
        sum += Integer.parseInt(bits[i]);
        //나눠진 문자열들을 순회하는 반복문 bits[0]~[i] 까지 순회해서 그 문자열들을 정수형으로 바꿔주기 위해
        //바뀐 정수형 문자들을 만들어놓은 합의 공간에 넣어주는 행위
        //bits[i]까지 순회하는 이유 지금까지는 2항연산 2개의 숫자의 합만을 계산했지만 3개 또는 4개  그 후에 합까지도 올바르게 작동할수
        //있어야 한다 -도 + - 로 치환 했기 떄문에 모든 문자열은 +를 기준으로 나뉘게 되고 조각들이 3개 4개가 될수 있는 경우도 있지만
        //순회하는 종료지점 bits.length를 사용하여 문자열이 몇개로 나뉘어지든 그 모든 수들을 정수화 시켜서 합에 더해주기 때문에 문제없이 작동할수 있다
      }

      return sum;//더하기를 요구하는 상황에서 합을 구해 리턴해준다 결과를 돌려주는 행위
      //이제 x + y 형의 더하기는 그때그떄 테스트케이스를 만들 필요없이 변수화 시켜서 어떤 수든 가능하게 만들어 주는기능
    } else if (needToMultiply) {//앞서 boolean값 트루일때 즉 *을 포함하고 있을때를 상정해서
      String[] bits = exp.split(" \\* ");
      //입력받은 문자열을 별을 기준으로 나누어 준다

      int rs = 1;// 결과를 담을 임의의 공간 1로 설정한 이유는 1은 어떤수를 곱해도 값에 영향을 주지 않는다 현재 상황은 곱을 리턴해줘야 하기 때문

      for (int i = 0; i < bits.length; i++) {
        rs *= Integer.parseInt(bits[i]);
        //플러스 상황과 동일하게 항이 몇개든 곱하기로만 이루어진 다항 계산은 곱하기 연산기호로 나누고 그 조각들을 i값으로 순회하며 조각을담은 문자열의 길이 크기보다 1적게 순회하도록
        //인덱스는 0부터 시작이니까 결국 모든 문자열을 순회하게 된다 이는 여러항이 생겻을때도 문제가 되지 않기에 다항 곱하기가 가능해진다
        //그 모든 문자열들의 곱을 rs에 담아서 리턴해 주게 된다
        //곱하기끼리는 순서가 상관 없기에
      }
      return rs;
    }

    throw new RuntimeException("처리할 수 있는 계산식이 아닙니다");//위 모든상황에도 부합하지 않는 테스트케이스가 있을수 있기에
    //그럴때는 리턴해줄수 있는 int 자로가 없어서 혹시모를 상황을 대비하기 위해
  }

  private static int findSplitPointIndexBy(String exp, char findChar) {
    int bracketCount = 0;

    for (int i = 0; i < exp.length(); i++) {
      char c = exp.charAt(i);

      if (c == '(') {
        bracketCount++;
      } else if (c == ')') {
        bracketCount--;
      } else if (c == findChar) {
        if (bracketCount == 0) return i;
      }
    }
    return -1;
  }

  private static int findSplitPointIndex(String exp) {
    int index = findSplitPointIndexBy(exp, '+');

    if (index >= 0) return index;

    return findSplitPointIndexBy(exp, '*');
  }

  private static String stripOuterBracket(String exp) {//첫부분 입력받은 문자의 괄호를 제거해 주기 위해 만든 메서드
    int outerBracketCount = 0;//괄호를 몇번 만났는지 새려고 만들어둔것 최초에 입력받은 문자열이 잇겟지만 괄호가 있는지 없는지
    //아직 체크하지 않았기에 어떤수에도 영향을 주지 않을 0으로 초기화 시켜놓는다


    while (exp.charAt(outerBracketCount) == '(' && exp.charAt(exp.length() - 1 - outerBracketCount) == ')') {
      outerBracketCount++;
    }
    // 반복문을 통해서 브라켓 카운트로 charAt 에 카운트를 준다 0부터 시작해서 입력받은 문자열의 크기보다 1작게 즉 문자열의 길이인데
    //인뎃스형식으로 카운트 하기 위해 그리고 그떄 ( )이 존재한다면 카운트를 1 늘려준다
    if (outerBracketCount == 0) return exp;
    //브라켓 카운트가 0이라면 문자열을 순회하는 동안 괄호를 만난 적이 없엇다 즉 입력받은 문자열에는 괄호가 없다 그렇기에 입력받은
    //그대로 돌려주게 된다

    //아래는 else의 상황 브라켓 카운트가 1이라도 올라갓다면 문자열을 감싸고 있는 괄호가 존재한다는 이야기
    //substring을 통해 브라켓카운트가 증가한만큼 괄호도 쌍으로 존재할것이기에 괄호다음부터 괄호 직전까지를 문자를 추출해 내는 행위
    return exp.substring(outerBracketCount, exp.length() - outerBracketCount);
  }
}