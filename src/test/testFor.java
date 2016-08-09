package test;

public class testFor {

	public static void main(String[] args) {
		for(int i=0;i<10;i++){
			
			try {
				if(i==5){
					i=1/0;
				}
				System.out.println(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}

	}

}
