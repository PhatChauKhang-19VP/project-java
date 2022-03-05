package app.user;

import app.App;
import app.product.Package;
import app.product.Product;

import java.util.ArrayList;
import java.util.Comparator;

public class Manager extends UserDecorator {
    public Manager(IUser user) {
        super(user);
    }

    //! 1.2.1 Patient management
    public void showPatientList() {
        if (getRole() == Role.MANAGER) {
            App.getInstance().getUserList().forEach(user -> {
                if (user.getRole() == Role.PATIENT) {
                    user.showInfo();
                }
            });
        }
    }

    public void showPatientInfo(IUser patient) {
        if (getRole() == Role.MANAGER) {
            if (patient.getRole() == Role.PATIENT) {
                patient.showInfo();
            } else {
                System.out.println("not a patient");
            }
        } else {
            System.out.println("You are not the manager");
        }
    }

    public Patient findPatientWithId(String id) {
        if (getRole() == Role.MANAGER) {
            for (IUser user : App.getInstance().getUserList()) {
                if (user.getRole() == Role.PATIENT && user.getUsername().equals(id)) {
                    return (Patient) user;
                }
            }
            return null;
        }
        return null;
    }

    public void sortPatientList(Comparator comparator) {
        if (getRole() == Role.MANAGER) {
            //todo: sort patient list by comparator
            System.out.println("Patient list is sorted");
        }
    }

    //! 1.2.2 add covid patient
    public boolean addPatient(Patient newPatient) {
        if (getRole() == Role.MANAGER) {
            try {
                // todo: add to database
            } catch (Exception e) {
                // err
                return false;
            } finally {
                App.getInstance().addUser(newPatient);
                return true;
            }
        }
        return false;
    }

    //! 1.2.3 update patient status
    public boolean updateStatus(Patient patient, int newStatus) {
        if (getRole() == Role.MANAGER) {
            patient.setStatus(newStatus);
            return true;
        }
        return false;
    }

    //! 1.2.4 product manager
    public Product findProductByName(String name) {
        return App.getInstance().getProductManagement().findProductByName(name);
    }

    public void sortProductList(Comparator comparator) {
        App.getInstance().getProductManagement().sortProductList(comparator);
    }

    public ArrayList<Product> filterProduct(/*filter*/) {
        if (getRole() == Role.MANAGER) {
            return App.getInstance().getProductManagement().filterProduct(/*filter*/);
        }
        return null;
    }

    public boolean addProduct(Product product) {
        if (getRole() == Role.MANAGER) {
            try {
                //add to DB
            } catch (Exception e) {
                return false;
            } finally {
                // update in App
                App.getInstance().getProductManagement().addProduct(product);
                return true;
            }
        }
        return false;
    }

    public boolean deleteProduct(String id) {
        if (getRole() == Role.MANAGER) {
            try {
                // delete product in DB
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            } finally {
                //update in Class App

                return App.getInstance().getProductManagement().deleteProduct(id);
            }
        }
        return false;
    }

    public boolean updateProduct(String productId, Product newProductInfo) {
        if (getRole() == Role.MANAGER) {
            try {
                // modify DB
            } catch (Exception e) {
                return false;
            } finally {
                // update in App
                return App.getInstance().getProductManagement().updateProduct(productId, newProductInfo);
            }
        }
        return false;
    }

    //! 1.2.5 package management
    public void showPackageList() {
        if (getRole() == Role.MANAGER) {
            // show package on UI

            App.getInstance()
                    .getProductManagement()
                    .getPackageList()
                    .forEach(aPackage -> {
                        System.out.println(aPackage);
                    });
        }
    }

    public Package findPackageByName(String name) {
        if (getRole() == Role.MANAGER) {
            return App.getInstance().getProductManagement().findPackageByName(name);
        }
        return null;
    }

    public void sortPackageList(Comparator comparator) {
        if (getRole() == Role.MANAGER) {
            App.getInstance().getProductManagement().sortPackageList(comparator);
        }
        return;
    }

    public ArrayList<Package> filterPackage(/*filter*/) {
        if (getRole() == Role.MANAGER) {
            return App.getInstance().getProductManagement().filterPackage(/*filter*/);
        }
        return null;
    }

    public boolean addPackage(Package pkg) {
        if (getRole() == Role.MANAGER) {
            try {
                //add package to DB
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            } finally {
                //update in class App
                App.getInstance().getProductManagement().addPackage(pkg);

                return true;
            }
        }
        return false;
    }

    public boolean updatePackage(String id, Package newPackageInfo) {
        if (getRole() == Role.MANAGER) {
            try {
                // update package info in DB
            } catch (Exception e) {
                System.out.println(e.getMessage());

                return false;
            } finally {
                return App.getInstance().getProductManagement().updatePackage(id, newPackageInfo);
            }
        }
        return false;
    }

    public boolean deletePagekage(String id) {
        if (getRole() == Role.MANAGER) {
            try {
                //delete package in DB
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            } finally {
                return App.getInstance().getProductManagement().deleteProduct(id);
            }
        }
        return false;
    }

    //! 1.2.6
    public void showStatic(String option) {
        if (getRole() == Role.MANAGER) {
            switch (option) {
                // show UI or Static here
                case "default":
                    return;
            }
        }
    }
}