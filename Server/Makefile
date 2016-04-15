
DIR_INC = ./include/
DIR_SRC = ./src
DIR_OBJ = ./obj
DIR_BIN = ./bin


SRC = $(wildcard ${DIR_SRC}/*.cpp)
OBJ = $(patsubst %.cpp, ${DIR_OBJ}/%.o, $(notdir $(SRC)))

TARGET = main.out

BIN_TARGET = ${DIR_BIN}/${TARGET}

CC = arm-linux-gnueabihf-g++

CFLAGS = -g -Wall -I ${DIR_INC}  -lwiringPi -std=c++11

${BIN_TARGET}:${OBJ}
	$(CC) $(OBJ) -o  $@ -lwiringPi

${DIR_OBJ}/%.o:${DIR_SRC}/%.cpp
	$(CC) ${CFLAGS} -c $< -o $@

.PHONY:clean
clean:
	rm ./obj/*.o ./bin/*
