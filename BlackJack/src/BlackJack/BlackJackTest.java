package BlackJack;

import java.util.*;
public class BlackJackTest {
	static Scanner scan = new Scanner(System.in);
	static int win = 0;		// 승 표시 필드
	static int lose = 0; 	// 패 표시 필드
	public static void main(String[] args) {
		
		BlackJack b = new BlackJack();
		System.out.println("블랙잭 시작!");
		while (true) {
			b.showMenu();
			System.out.print("└선택: ");
			int select = scan.nextInt();
			if (select == 1) {
				if(b.getCnt() == 0) {
					b.choiceMy();
					b.show();
				}else {
					System.out.println("카드 생성은 한번만 할 수 있습니다.");
				}
			} else if (select == 2) {
				b.Hit();
				b.show();
			} else if (select == 3) {
				b.Stay();
				showWinLose();
				System.out.print("처음부터 다시 하시겠습니까?(y/n): ");
				String c = scan.next();
				if (c.equals("y")) {
					b = new BlackJack();
					System.out.println("\n------다시 시작------");
				} else if (c.equals("n")) {
					break;
				} else {
					System.out.print("잘못입력, 프로그램을 종료합니다.");
					System.exit(0);
				}
				System.out.println();
			}else if(select == 4) {
				b.showRule();
			}else {
				System.out.println("없는 항목입니다.");
				System.out.println();
			}
		}
		System.out.println("프로그램을 종료합니다.");
	}
	public static void showWinLose() {
		float sum = win+lose;
		float per = (win/sum)*100;
		System.out.println(win+"승 "+lose+"패.");
		System.out.print("승률: "+String.format("%.2f",per)+"%\n");
	}
}

class BlackJack {
	private final String[] card = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
	// 나누어 줄 카드의 종류
	private String[] strCom = new String[10];	// 딜러의 카드 보유 배열
	private String[] strMy = new String[10];	// 유저의 카드 보유 배열
	private int[] comCard = new int[10];		// 딜러 카드의 합을 구하기 위한 배열
	private int[] myCard = new int[10];			// 유저 카드의 합을 구하기 위한 배열
	private int aindex;							// 카드를 랜덤으로 뽑기 위해 랜덤으로 초기화 할 필드
	private int bindex;							// 카드를 랜덤으로 뽑기 위해 랜덤으로 초기화 할 필드
	private String a;							// 카드를 뽑고 카드 보유 배열에 저장하기 위한 필드
	private String b;							// 카드를 뽑고 카드 보유 배열에 저장하기 위한 필드
	private int sumCom;							// 딜러 카드의 합
	private int sumMy;							// 유저 카드의 합
	private int cntMy = 2;						// 유저 보유 카드의 수를 저장하는 필드
	private int cntCom = 2;						// 딜러 보유 카드의 수를 저장하는 필드
	private int cnt;							// 카드 생성 제한을 위한 필드					
	
	BlackJack() {	// 객체 생성 시 딜러의 카드를 생성자로 정해줌
		cnt = 0;	// 유저 카드 생성 제한을 초기화
		aindex = (int) (Math.random() * 13);	// 랜덤으로 카드를 뽑기위해 random사용
		bindex = (int) (Math.random() * 13);
		a = card[aindex];	// 랜덤으로 카드 뽑기
		b = card[bindex];
		strCom[0] = a;		// 뽑은 카드를 카드 보유 배열에 저장
		strCom[1] = b;
		int i = 2;			// 딜러 카드의 보유 수 (카드의 합이 17이하일 경우 더 뽑기 위함)
		if (a.equals("A")) {	// 딜러의 첫 카드가 "A"일 때 경우의 수
			if (b.equals("10") || b.equals("J") || b.equals("Q") || b.equals("K")) {
				comCard[0] = 11;		// 두번째 카드가 10 or 문자일 경우 11,10 저장
				comCard[1] = 10;
			} else if (Integer.parseInt(b) == (bindex + 1)) {
				if (bindex > 3) {		// 두번째 카드가 숫자일 경우
					comCard[0] = 11;	// A카드 1or11 결정을 위함
				} else
					comCard[0] = 1;
				comCard[1] = bindex + 1;	// 두번째 카드는 그대로 저장
			} else if (b.equals("A")) {	// 두번째 카드도 "A"일 경우
				comCard[0] = 11;		// 11,1로 저장
				comCard[1] = 1;
			}
		} else if (a.equals("10") || a.equals("J") || a.equals("Q") || a.equals("K")) {
			comCard[0] = 10;	// 딜러의 첫 카드가 10or문자일 경우 10으로 저장
			if (b.equals("A")) {	// 두번째 "A"일 경우 11로 저장하여 블랙잭
				comCard[1] = 11;
			} else if (b.equals("10") || b.equals("J") || b.equals("Q") || b.equals("K")) {
				comCard[1] = 10;	// 두번째도 10or문자일 경우 10으로 저장
			} else
				comCard[1] = bindex + 1;	// 두번째가 숫자일 경우 숫자 그대로 저장
		} else {
			comCard[0] = aindex + 1;		// 딜러의 첫 카드가 숫자일 경우 그대로 저장
			if (b.equals("A")) {			// 두번째가 "A"일 경우 첫 카드의 크기에 따라
				if (aindex > 3) {			// "A"카드의 수를 1or11로 결정
					comCard[1] = 11;
				} else
					comCard[1] = 1;
			} else if (b.equals("10") || b.equals("J") || b.equals("Q") || b.equals("K")) {
				comCard[1] = 10;			// 두 번째 카드가 10or문자일 경우 10으로 저장
			} else
				comCard[1] = bindex + 1;	// 두 번째 카드도 숫자일 경우 숫자 그대로 저장
		}
		sumCom = comCard[0] + comCard[1];	// 생성한 카드의 합을 저장
		while (true) {
			if (sumCom < 17) {	// 딜러 카드의 합이 17을 넘을 때 까지 
				moreCom(i);		// 카드를 뽑기 위한 반복문
			} else
				break;
		}
	}
	
	public int getCnt() {return cnt;}	// 유저 카드생성 제한확인을 위한 get메소드
	
	public void showMenu() {	// 메뉴 표시
		System.out.println("┌───────────────────┐");
		System.out.println("    1.카드 생성  |  2.Hit  |  3.Stay  |  4.규칙");
		System.out.println("└───────────────────┘");
	}

	public void show() {	// 딜러의 첫 카드와 user의 보유 카드 표시
		System.out.println("컴카드: " + strCom[0]);
		for (int i = 0; i < strCom.length; i++) {
			if (strMy[i] != null)
				System.out.print("내카드: " + strMy[i] + "\t");
		}
		System.out.println();
		System.out.println("내카드 합: " + sumMy);
		System.out.println();
	}

	public void showCom() {	// 결과 발표시 딜러의 카드 보유 사항 공개
		for (int i = 0; i < strCom.length; i++) {
			if (strCom[i] != null)
				System.out.print("딜러카드: " + strCom[i] + "\t");
		}
		System.out.println();
		System.out.println("딜러카드 합: " + sumCom);
	}
	
	public void showRule() {	// 게임 규칙 설명
		System.out.println("-------------------------게임 설명-------------------------");
		System.out.println("1. 더블다운, 스플릿, 인셔런스, 이븐머니 같은 경우는 제외");
		System.out.println("2. 기본 룰만 약간 변경되어 게임을 진행합니다.");
		System.out.println("3. 카드 생성으로 User와 딜러는 처음 카드 2장을 받고");
		System.out.println("4. 딜러는 첫번째 카드를 오픈합니다.(카드 생성은 처음 한번만 가능합니다.)");
		System.out.println("5. 카드의 수는 제한이 없어 같은 카드를 2장 이상 받을 수 있습니다.");
		System.out.println("6. A를 두장 받을 경우 처음 카드는 무조건 11, 두번째 카드는 무조건 1입니다.");
		System.out.println("7. Hit 선택시 User는 카드 한장을 받게 됩니다.");
		System.out.println("8. A를 받을 경우 처음 한번만 1 or 11로 선택할 수 있고 추후 변경이 불가합니다.");
		System.out.println("9. Stay 선택시 카드를 더 이상받지 않게 되므로 결과가 나옵니다.");
		System.out.println();
		System.out.println("-------------------------결과 발표-------------------------");
		System.out.println("1. 결과 발표시 딜러는 카드의 합이 17이 넘을때 까지 카드를 새로 받습니다.");
		System.out.println("2. 처음 카드 생성 후 바로 카드 합이 21일 경우 블랙잭! 입니다.");
		System.out.println("3. 모든 카드의 합이 21과 가까운 사람이 승리하게 되고 21을 넘을 경우 패배하게 됩니다.");
		System.out.println("4. 딜러, User모두 21을 넘을 경우 카드를 많이 보유한 사람이 패배하게 됩니다.");
		System.out.println("5. 딜러, User모두 21을 넘고 카드수도 같은 경우 카드의 합이 높은 사람이 패배합니다.");
		System.out.println("6. 딜러, User모두 21을 넘지 않고 카드의 합이 같은 경우는 무승부 입니다.");
		System.out.println();
	}
	
	public void moreCom(int m) {	// 딜러 카드의 합이 17을 넘을 때 까지 뽑는 메소드
		int nindex = (int) (Math.random() * 13);
		String n = card[nindex];
		strCom[m] = n;
		if (n.equals("10") || n.equals("J") || n.equals("Q") || n.equals("K")) {
			comCard[m] = 10;
		} else
			comCard[m] = nindex + 1;
		sumCom += comCard[m];
		cntCom++;
	}

	public void choiceMy() {		// 유저의 카드 생성 메소드 (딜러와 동일, "A"카드 선택사항만 다름)
		aindex = (int) (Math.random() * 13);
		bindex = (int) (Math.random() * 13);
		a = card[aindex];
		b = card[bindex];
		strMy[0] = a;
		strMy[1] = b;
		if (a.equals("A")) {
			if (b.equals("10") || b.equals("J") || b.equals("Q") || b.equals("K")) {
				myCard[0] = 11;
				myCard[1] = 10;
			} else if (Integer.parseInt(b) == (bindex + 1)) {
				myCard[0] = 11;
				myCard[1] = bindex + 1;
			} else if (b.equals("A")) {
				myCard[0] = 11;
				myCard[1] = 1;
			}
		} else if (a.equals("10") || a.equals("J") || a.equals("Q") || a.equals("K")) {
			myCard[0] = 10;
			if (b.equals("A")) {
				myCard[1] = 11;
			} else if (b.equals("10") || b.equals("J") || b.equals("Q") || b.equals("K")) {
				myCard[1] = 10;
			} else
				myCard[1] = bindex + 1;
		} else {
			myCard[0] = aindex + 1;
			if (b.equals("A")) {
				if (aindex > 3) {
					myCard[1] = 11;
				} else
					myCard[1] = 1;
			} else if (b.equals("10") || b.equals("J") || b.equals("Q") || b.equals("K")) {
				myCard[1] = 10;
			} else
				myCard[1] = bindex + 1;
		}
		if(a.equals("A")||b.equals("A")) {	// "A"가 나왔을 경우 1or11선택
			System.out.println("("+strMy[0]+", "+strMy[1]+")");
			System.out.print("A의 수를 선택해 주세요(1 or 11): ");
			int aa = BlackJackTest.scan.nextInt();
			if(aa==1) {
				if(a.equals("A")) {
					myCard[0]=1;
				}else if(b.equals("A")) {
					myCard[1]=1;
				}
			}else if(aa==11) {
				if(a.equals("A")) {
					myCard[0]=11;
				}else if(b.equals("A")) {
					myCard[1]=11;
				}
			}else System.out.println("잘못입력하여 기본값대로합니다.");	// 1or11아닌 값을 입력 시 출력
		}
		sumMy = myCard[0] + myCard[1];	// 유저 카드의 합
		cnt++;		// 카드 생성 제한을 위함
		System.out.println("카드 생성완료");
	}

	public void Hit() {		// 카드를 새로 뽑기 위한 메소드 (생성과 동일하나 한장만 뽑음)
		for (int i = 0; i < strMy.length; i++) {
			if (strMy[i] == null) {
				aindex = (int) (Math.random() * 13);
				a = card[aindex];
				strMy[i] = a;
				if (a.equals("10") || a.equals("J") || a.equals("Q") || a.equals("K")) {
					myCard[i] = 10;
				} else if(a.equals("A")) {	// "A"가 나왔을 경우 1or11 선택을 위함
					System.out.print("A의 수를 선택해 주세요(1 or 11): ");
					int aa = BlackJackTest.scan.nextInt();
					if(aa==1) {
							myCard[i]=1;
					}else if(aa==11) {
							myCard[i]=11;
					}else System.out.println("잘못입력하여 기본값대로합니다.");
				}else
					myCard[i] = aindex + 1;
				sumMy += myCard[i];
				cntMy++;	// 보유 카드 수 증가
				break;
			}
		}
	}

	public void Stay() {	// 카드를 받지 않고 결과를 보여주는 메소드
		showCom();
		if (sumMy == sumCom) {
			System.out.println("무승부 입니다.");
		} else {
			if (sumCom == 21) {	// 딜러의 블랙잭 확인
				if (cntCom == 2) {
					System.out.println("딜러의 블랙잭~!");
				} else			// 딜러가 21과 가까우므로 딜러 승리
					System.out.println("딜러 승리!");
				BlackJackTest.lose++;
			} else if (sumMy == 21) {
				if (cntMy == 2) {// 유저의 블랙잭 확인
					System.out.println("User이 블랙잭~!");
				} else			// 유저가 21과 가까우므로 유저 승리
					System.out.println("User 승리!");
				BlackJackTest.win++;
			} else if (sumCom > 21) {	// 딜러가 21을 넘엇을경우
				if (sumMy > 21) {		// 유저가 21을 넘엇을 경우
					if (cntMy > cntCom) {	// 카드 수에 따라 승리 결정
						System.out.println("딜러 승리!");
						BlackJackTest.lose++;
					} else if (cntMy < cntCom) {
						System.out.println("User 승리!");
						BlackJackTest.win++;
					} else {	// 카드 수도 동일할 경우 합으로 승리 결정
						if (sumMy > sumCom) {
							System.out.println("딜러 승리!");
							BlackJackTest.lose++;
						} else if (sumMy < sumCom) {
							System.out.println("User 승리!");
							BlackJackTest.win++;
						} else
							System.out.println("무승부 입니다.");
					}
				} else { // 딜러만 21을 넘엇을 경우
					System.out.println("User 승리!");
					BlackJackTest.win++;
				}
			} else if (sumMy > 21) {// 유저가 21을 넘엇을 경우
				System.out.println("딜러 승리!");
				BlackJackTest.lose++;
			} else if(sumMy<21&&sumCom<21){	// 딜러, 유저 모두 21보다 작을 경우
				if ((21 - sumCom) > (21 - sumMy)) {	// 21과 가까운 사람이 승리 결정
					System.out.println("User 승리!");
					BlackJackTest.win++;
				} else if ((21 - sumCom) < (21 - sumMy)) {
					System.out.println("딜러 승리!");
					BlackJackTest.lose++;
				}
			}
		}
	}
}