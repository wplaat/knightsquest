# Quick'n'dirty makefile [BC]

ifeq ($(strip $(DEVKITPPC)),)
  $(error "Use   export DEVKITPPC=<path to>devkitPPC   and try again")
endif

ifeq ($(strip $(DEVKITPRO)),)
  $(error "Use   export DEVKITPRO=<path to>devkitPRO   and try again")
endif

OGC := $(DEVKITPRO)/libogc

HDR := $(shell ls include/*.h)
ARC := $(shell ls lib/wii/*.a)

.PHONY : all install

all :
	@echo -e "\nWe must track down the svn repo for the source"
	@echo "  and add it as an SVN Vendor Branch"
	@echo "    http://svnbook.red-bean.com/en/1.1/ch07s05.html"
	@echo "...For now, all you get is precompiled libraries"

install :
	mkdir -p  $(OGC)/lib/wii/  $(OGC)/include/
	cp -rf  $(ARC)  $(OGC)/lib/wii/
	cp -rf  $(HDR)  $(OGC)/include/
