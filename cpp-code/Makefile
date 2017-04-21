CXXFLAGS =	-O2 -g -Wall -fmessage-length=0

OBJS =		TreeMarkupToolbox.o

LIBS =

TARGET =	TreeMarkupToolbox

$(TARGET):	$(OBJS)
	$(CXX) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
