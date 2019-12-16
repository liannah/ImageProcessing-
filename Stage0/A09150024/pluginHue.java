import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;


public class pluginHue implements PlugInFilter {

public int setup(String args, ImagePlus im) {
	return DOES_ALL;
}

public void run(ImageProcessor ip) {

int M = ip.getWidth();
 int N = ip.getHeight();
int l = 256;

int[] H = ip.getHistogram();
double[] H1 = new double[]{3018,	173,	300,	477,	278,	666,	583,	1586,	2772,	1482,	2656,	3525,	3841,	4929,	6380,	7323,	7435,	4056,	8902,	2267,	1670,	1029,	537,	589,	182,	23,	1201,	6,	2718,	391,	25,	136,	0,	30,	3075,	665,	133,	29,	25,	27,	55,	1,	4478,	0,	15,	34,	24,	33,	7,	281,	10,	2335,	13,	5,	8,	0,	7,	0,	2,	265,	28,	0,	0,	0,	0,	0,	0,	1,	0,	0,	776,	0,	5,	0,	0,	0,	0,	50,	0,	6,	0,	0,	0,	0,	0,	16,	0,	0,	0,	0,	0,	0,	0,	371,	0,	0,	1,	0,	0,	49,	0,	0,	0,	3,	1,	0,	529,	0,	0,	0,	0,	0,	9,	149,	0,	0,	432,	0,	1,	1,	18,	0,	28,	33,	2,	0,	0,	226,	0,	1,	42,	322,	0,	0,	1540,	317,	1,	1,	153,	554,	15,	3,	445,	114,	7,	76,	420,	569,	33,	0,	675,	3,	28,	6,	143,	123,	109,	98,	27,	46,	0,	67,	50,	11,	2,	0,	1,	0,	0,	0,	210,	0,	0,	1,	0,	9,	1,	12,	45,	0,	1,	1,	0,	0,	4,	0,	0,	18,	6,	0,	0,	0,	0,	0,	0,	0,	0,	0,	107,	0,	3,	0,	0,	0,	0,	7,	0,	1,	0,	0,	0,	0,	8,	0,	0,	0,	1,	0,	0,	0,	0,	36,	0,	0,	1,	0,	19,	0,	0,	0,	0,	12,	0,	571,	0,	0,	0,	0,	0,	12,	134,0,0,1,211,1,20,25,6,25,285,71,131,88,66,0,};


double[] Green = new double[H.length];
Green[0] = H[0];

for (int i = 1; i < H.length; i++){
    H[i] = H[i-1] + H[i];
    Green[i] = H[i];	
    H1[i] = H1[i -1] + H1[i];	
}

for (int i = 0; i < l; i++){
     Green[i] = Green[i] / Green[l-1];
     H1[i] = H1[i] / H1[l-1]; 	
}


int[] result = matchHistograms(Green, H1);

for (int v = 0; v < N; v++) {
 for (int u = 0; u < M; u++) {
 int a = ip.get(u, v);
 int b = result[a];
 ip.set(u, v, b);
}
}

}

public int[] matchHistograms (double[] hA, double[] hR) {

int K = hA.length;
 double[] PA = Cdf(hA); // get CDF of histogram hA
 double[] PR = Cdf(hR); // get CDF of histogram hR
 int[] fhs = new int[K]; // mapping fhs()

 // compute mapping function fhs():
 for (int a = 0; a < K; a++) {
 int j = K - 1;
 do {
 fhs[a] = j;
 j--;
 } while (j >= 0 && hA[a] <= hR[j]);
 }
 return fhs;
 }



public double[] Cdf (double[] h) {
 // returns the cumul. distribution function for histogram h
 int K = h.length;

 int n = 0; // sum all histogram values
 for (int i = 0; i < K; i++) {
 n += h[i];
 }

 double[] P = new double[K]; // create CDF table P
 double c = h[0]; // cumulate histogram values
 P[0] = (double) c / n;
 for (int i = 1; i < K; i++) {
 c += h[i];
 P[i] = (double) c / n;
 }
 return P;
 }


}