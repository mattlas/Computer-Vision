function [R, t] = rigidalign( P, Q )
%%% Classification digit from image.
%
%   rigidalign( P, Q ), 
%   where P, Q in R(dxn) represent the point sets (one point per column). 
%   Function returns a rotation matrix R and a translation vector t of
%   vector P which tranformed into Q. It means   
%                       Q = R*P + t.
%   
%   WORKING EXAMPLE: Determind rotation matrix R and translation vector t
%                    of transfomation of P into Q.
%                   [ 1 1  2 ]
%               P = [ 1 2  2 ];                  
%
%                   [ 1.9497 2.3592  3.2715 ]
%               Q = [ 1.2748 2.1871  1.7776 ];                   
%
%   [R, t] = rigidalign( P, Q )
%   => R = [0.9213 0.4095; -0.4095 0.9123] and t = [0.6279; 0.7720]
%Author: Quoc Khanh Nguyen, ens16knc@cs.umu.se 2016-10-08.
%%%%Function code starts here ...
    
    [d, n]=size(P); %Defination of matrix P and Q
    
    %Initialization
    q_cm = zeros(d,1); % Vector center of mass of Q
    p_cm = zeros(d,1); % Vector center of mass of P
    X = zeros(2,n); %Distance vectors between vertexs and center of mass of P 
    Y = zeros(2,n); %Distance vectors between vertexs and center of mass of Q
    
    %Calculate center of mass vector of P and Q
    for i = 1 : d
        p_cm(i) = (1/n)*sum(P(i,:));
        q_cm(i) = (1/n)*sum(Q(i,:));  
    end
    %Calculate distance vectors  between vertexs and center of mass
    for i = 1:n
        X(:,i) =  P(:,i) - p_cm;
        Y(:,i) =  Q(:,i) - q_cm;
    end
    
    S = X * Y'; %covariance matrix
    [U, ~, V] = svd(S); %Compute the singular value decomposition
    
    %Compute the optimal rotation matrix R
    I = eye(d);
    I(d,d) = det(V*U');
    R = V * I * U';
    
    %Compute the optimal translation vector t
    t = q_cm - (R*p_cm);
end