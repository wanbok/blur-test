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

  uchar4 *head = in + *v_in * nx;
  uchar4 *tail = head;
  uchar4 *p = out + *v_in * nx;    

  uchar4 *hpw = head + W;
  uchar4 *hpn = head + N;
  uchar4 *hpx = head + nx;
  uchar4 *hpxmw = head + nx - W - 1;    

  while (head < hpw) {
      sum += rsUnpackColor8888(*head++);
  }    

  while (head < hpn) {
      sum += rsUnpackColor8888(*head++);
      *p++ = rsPackColorTo8888(sum*invN);
  }    

  while (head < hpx) {
    sum += rsUnpackColor8888(*head++);
    sum -= rsUnpackColor8888(*tail++);
    *p++ = rsPackColorTo8888(sum*invN);
  }    

  while (tail < hpxmw) {
      sum -= rsUnpackColor8888(*tail++);
      *p++ = rsPackColorTo8888(sum*invN);
  }
}