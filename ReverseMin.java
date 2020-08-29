//author Margarita Shimanskaia
//returns for each matrix element min in its row and column
import java.util.*;

public class ReverseMin {
     public static void main(String[] args) {

		List<Integer> rowMin = new ArrayList<Integer>();
		List<Integer> colMin = new ArrayList<Integer>();
		List<Integer> rowSize = new ArrayList<Integer>();

        Scanner in = new Scanner(System.in);
		int iRow = 0;
		while (in.hasNextLine()) {
			String nxl = in.nextLine();
			Scanner stin = new Scanner(nxl);
			int iCol = 0;
			while ( stin.hasNextInt() ) {
				int k = stin.nextInt();
				if (iRow < rowMin.size()) {
					if (k < rowMin.get(iRow))
						rowMin.set(iRow,k);
				}
				else
					rowMin.add(k);
				if (iCol < colMin.size()) {
					if (k < colMin.get(iCol))
						colMin.set(iCol,k);
				}
				else
					colMin.add(k);					
				iCol++;
			}
			if (iCol == 0)
				rowMin.add(0);	//Fill any value for empty line
			rowSize.add(iCol);	//Keep row size
			iRow++;
		}

		for (int i=0; i < rowSize.size() ; i++) {
			for (int j=0; j < rowSize.get(i) ; j++) {
				System.out.print(Math.min(rowMin.get(i), colMin.get(j)) + " ");
			}	
			System.out.println();
        }
	}
}  