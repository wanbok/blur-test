#pragma version(1)
#pragma rs java_package_name(com.mintech.blur)
#include "rs_graphics.rsh"

int W = 8;
uchar4 *in;
uchar4 *out;    

int N;
float invN;    

uint32_t nx;
uint32_t ny;

void init_calc() {
  N = 2*W+1;
  invN = 1.0f/N;    

  nx = rsAllocationGetDimX(rsGetAllocation(in));
  ny = rsAllocationGetDimY(rsGetAllocation(in));
}    

void root(const ushort *v_in) {
  float4 sum = 0;    

  uchar4 *head = in + *v_in;
  uchar4 *tail = head;
  uchar4 *hpw = head + nx*W;
  uchar4 *hpn = head + nx*N;
  uchar4 *hpy = head + nx*ny;
  uchar4 *hpymw = head + nx*(ny-W-1);    

  uchar4 *p = out + *v_in;    

  while (head < hpw) {
      sum += rsUnpackColor8888(*head);
      head += nx;
  }    

  while (head < hpn) {
      sum += rsUnpackColor8888(*head);
      *p = rsPackColorTo8888(sum*invN);
      head += nx;
      p += nx;
  }    

  while (head < hpy) {
      sum += rsUnpackColor8888(*head);
      sum -= rsUnpackColor8888(*tail);
      *p = rsPackColorTo8888(sum*invN);
      head += nx;
      tail += nx;
      p += nx;
  }    

  while (tail < hpymw) {
      sum -= rsUnpackColor8888(*tail);
      *p = rsPackColorTo8888(sum*invN);
      tail += nx;
      p += nx;
  }
}