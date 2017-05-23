#include <dirent.h>
#include <memory>
#include <iostream>
#include <cstring>
#include "DirectoryReader.h"

std::vector<std::string> DirectoryReader::readDirectory(const std::string &dir){

    std::vector < std::string > files;

    std::shared_ptr < DIR
			> directory_ptr(opendir(dir.c_str()),
					[](DIR *dir){dir && closedir(dir);});

	struct dirent *dirent_ptr;

	if(!directory_ptr){
		std::cout << "Error opening : " << std::strerror(errno) << dir
				<< std::endl;

        return files;
	}

	while((dirent_ptr = readdir(directory_ptr.get())) != nullptr){

        if((std::string(dirent_ptr->d_name)).front() != '.'){
			std::string pathName = dir;

            if(pathName.back() != '/'){
				pathName.append("/");
			}

            pathName.append(std::string(dirent_ptr->d_name));
			files.push_back(pathName);
		}
	}
	return files;
}
