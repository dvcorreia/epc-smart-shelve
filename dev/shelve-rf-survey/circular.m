close all; clear; clc;
load('data.mat');

data(isnan(data)) = Inf;
data = -1 * data;


x = linspace(0, 150, size(data, 1));
y = linspace(0, 60, size(data, 2));
z = [0, 250, 500, 700, 950, 1200, 1430, 1600];

fig1 = figure; hold on; grid minor;
set(gca, 'XMinorGrid', 'on')
set(gca, 'YMinorGrid', 'on')
view(-16.5,20);

title('Obstruction Points','Interpreter','latex')
xlabel('x (cm)','Interpreter','latex')
ylabel('y (cm)','Interpreter','latex')
zlabel('z (cm)','Interpreter','latex')

[X, Y, Z] = meshgrid(x, y, z);

for i = 1 : length(z)
    XX = X(:,:,i);
    YY = Y(:,:,i); 
    ZZ = Z(:,:,i);
    DATA = data(:,:,i)';
    id = DATA < -80;
    scatter3(XX(id), YY(id), ZZ(id), 30, 'filled', 'b');
    scatter3(XX(id), YY(id), zeros(size(XX(id))), 10, 'r', 'x');
end

legend('Obstruction Points', 'Obstruction Points z=0', 'Interpreter','latex');
