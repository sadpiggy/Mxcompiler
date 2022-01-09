#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

void print(const char *s) {printf("%s", s);}

void println(const char *s) {printf("%s\n", s);}

void printInt(int n) {printf("%d", n);}

void printlnInt(int n) {printf("%d\n", n);}

void *_malloc(int n) {return malloc(n);}

const char *getString()
{
	char *stringMid = (char *)(malloc(sizeof(char) * 1024));
	scanf("%s", stringMid);
	return stringMid;
}

int getInt()
{
	int intMid;
	scanf("%d", &intMid);
	return intMid;
}

const char *toString(int i)
{
	char *stringMid = (char*)malloc(sizeof(char) * 15);
	sprintf(stringMid, "%d", i);
	return stringMid;
}

//前面是内建函数

const char *string_add(const char *lhs, const char *rhs)
{
	char *stringMid = (char*)malloc(sizeof(char) * 1024);
	strcpy(stringMid, lhs);
	strcat(stringMid, rhs);
	return stringMid;
}

bool string_equal(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) == 0;}

bool string_notequal(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) != 0;}

bool string_lesser(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) < 0;}

bool string_greater(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) > 0;}

bool string_lessEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) <= 0;}

bool string_greatEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) >= 0;}

int string_length(const char *s) {return strlen(s);}

const char *string_substring(const char *s, int l, int r)
{
	char *stringMid = (char*)malloc(sizeof(char) * (r - l + 1));
	memcpy(stringMid, s + l, r - l);
	stringMid[r - l] = '\0';
	return stringMid;
}

int string_parseInt(const char *s)
{
	int intMid;
	sscanf(s, "%d", &intMid);
	return intMid;
}

int string_ord(const char *s, int pos) {return s[pos];}

